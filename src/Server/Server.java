package Server;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable{
    private static Server _serverInstance;
    public static Server Instance(){
        if(_serverInstance == null){
            _serverInstance = new Server();
        }
        return _serverInstance;
    }

    private Server(){}
    private ServerSocket _serverSocket;

    private Thread _servThread;
    private List<Client> _clients = new ArrayList<>();
    private boolean running = false;

    public void start(){
        _servThread = new Thread(_serverInstance);
        _servThread.start();
        running = true;
    }

    public void stop() throws IOException{
        running = false;
        _serverSocket.close();
    }

    @Override
    public void run() {        
        try {
            _serverSocket = new ServerSocket(3333);
            while (running) {
                System.out.println("Waiting for client...");
                Socket clientSocket = _serverSocket.accept();
                System.out.println("Client connected!");

                var connectedClient = new Client(clientSocket, _serverInstance);
                _clients.add(connectedClient);
                connectedClient.start();

                if(Thread.interrupted()){
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Server stopped");
    }

    public void removeClient(Client client){
        _clients.remove(client);
    }

    public void broadcast(String message){
        for(var client : _clients){
            
        }
    }
}
