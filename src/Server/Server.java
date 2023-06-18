package Server;

import Game.GameObject;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {
    private static Server serverInstance;

    public static Server Instance() {
        if (serverInstance == null) {
            serverInstance = new Server();
        }
        return serverInstance;
    }

    private Server() {
    }

    private ServerSocket serverSocket;
    private Thread serverThread;
    private List<Client> clients = new ArrayList<>();
    private boolean running = false;

    public void start() {
        serverThread = new Thread(serverInstance);
        serverThread.start();
        running = true;
    }

    public void stop() {
        running = false;
        serverThread.interrupt();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(3333);
            while (running) {
                System.out.println("Waiting for clients...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                var connectedClient = new Client(clientSocket, serverInstance);
                clients.add(connectedClient);
                connectedClient.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Server stopped");
        }
    }

    public void removeClient(Client client) {
        clients.remove(client);
    }

    public void broadcast(ArrayList<GameObject> gameObjects) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(gameObjects);

            for (Client client : clients) {
                DataOutputStream dos = new DataOutputStream(client.getSocket().getOutputStream());
                dos.writeInt(baos.size());
                dos.write(baos.toByteArray());
                dos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     public static void main(String[] args) {
        Server.Instance().start();
    }
}
