import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private int numPlayers;
    private int maxPlayers;
    private Socket p1Socket;
    private Socket p2Socket;
    private ReadFromClient p1ReadRunnable;
    private ReadFromClient p2ReadRunnable;
    private WriteToClient p1WriteRunnable;
    private WriteToClient p2WriteRunnable;
    private double p1x, p1y, p2x, p2y;

    public Server(){
        System.out.println("===GAME SERVER===");
        numPlayers = 0;
        maxPlayers = 2;

        p1x = 100;
        p1y = 400;
        p2x = 490;
        p2y = 400;

        try {
            serverSocket = new ServerSocket(45371);
        } catch (IOException e) {
            throw new RuntimeException("Server constructor error");
        }
    }

    public void acceptConnection(){
        try{
            System.out.println("Waiting for connections...");

            while(numPlayers < maxPlayers){
                Socket socket = serverSocket.accept();
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

                numPlayers++;
                outputStream.writeInt(numPlayers);
                System.out.println("Players #" + numPlayers + " has connected");

                ReadFromClient readFromClient = new ReadFromClient(numPlayers,inputStream);
                WriteToClient writeFromClient = new WriteToClient(numPlayers, outputStream);

                if(numPlayers == 1){
                    p1Socket = socket;
                    p1ReadRunnable = readFromClient;
                    p1WriteRunnable = writeFromClient;
                } else {
                    p2Socket = socket;
                    p2ReadRunnable = readFromClient;
                    p2WriteRunnable = writeFromClient;
                    p1WriteRunnable.sendStartMsg();
                    p2WriteRunnable.sendStartMsg();

                    Thread readThread1 = new Thread(p1ReadRunnable);
                    Thread readThread2 = new Thread(p2ReadRunnable);
                    readThread1.start();
                    readThread2.start();

                    Thread writeThread1 = new Thread(p1WriteRunnable);
                    Thread writeThread2 = new Thread(p2WriteRunnable);
                    writeThread1.start();
                    writeThread2.start();
                }

            }
            System.out.println("No longer accepting connections");
        } catch (IOException e){
            System.out.println("IOException from acceptConnection()");
        }
    }

    private class ReadFromClient implements  Runnable {
        private int playerID;
        private DataInputStream dataInputStream;


        public ReadFromClient(int playerID, DataInputStream dataInputStream) {
            this.playerID = playerID;
            this.dataInputStream = dataInputStream;
            System.out.println("ReadFromClient " + playerID + "Runnable created");
        }

        @Override
        public void run() {
            try {
                while (true) {
                    if (playerID == 1) {
                        p1x = dataInputStream.readDouble();
                        p1y = dataInputStream.readDouble();
                    } else {
                        p2x = dataInputStream.readDouble();
                        p2y = dataInputStream.readDouble();
                    }
                }
            } catch (IOException e) {
                System.out.println("IOException from ReadFromClient");
            }
        }
    }

    private class WriteToClient implements  Runnable{
        private int playerID;
        private DataOutputStream dataOutputStream;


        public WriteToClient(int playerID, DataOutputStream dataOutputStream) {
            this.playerID = playerID;
            this.dataOutputStream = dataOutputStream;
            System.out.println("WriteToClient " + playerID + "Runnable created");
        }

        @Override
        public void run() {
            try{
                while (true){
                    if(playerID == 1){
                        dataOutputStream.writeDouble(p2x);
                        dataOutputStream.writeDouble(p2y);
                        dataOutputStream.flush();
                    } else {
                        dataOutputStream.writeDouble(p1x);
                        dataOutputStream.writeDouble(p1y);
                        dataOutputStream.flush();
                    }
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e){
                        System.out.println("InterruptedException from WriteToClient run()");
                    }
                }
            } catch (IOException e){
                System.out.println("IOException from WriteToClient");
            }
        }

        public void sendStartMsg(){
            try {
                dataOutputStream.writeUTF("There are 2 players. The game will begin.");

            } catch (IOException e) {
                System.out.println("IOException from sendStartMsg()");
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.acceptConnection();
    }
}
