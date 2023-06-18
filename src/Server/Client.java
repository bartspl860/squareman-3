package Server;

import Game.GameObject;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Client extends Thread{

	private Socket _socket;
	private Server _server;
	boolean _connected=true;				//dodałem boola do zarzadznia poloczeniem
	public Client(Socket soc, Server serv){
		this._socket = soc;
		this._server = serv;
	}

	@Override
	public void run(){
		try{
			while(_connected){
				DataInputStream dis = new DataInputStream(_socket.getInputStream());
				if(dis.available() > 0){
					int size = dis.readInt();
					byte[] data = new byte[size];
					dis.readFully(data);
					ByteArrayInputStream bais = new ByteArrayInputStream(data);
					ObjectInputStream ois = new ObjectInputStream(bais);
					ArrayList<GameObject> gameObjects = (ArrayList<GameObject>) ois.readObject();
					for (GameObject gameObject : gameObjects) {
                    System.out.println("Received game object: " + gameObject);
					}
					
				}
			}
		} catch (IOException | ClassNotFoundException e){
			e.printStackTrace();
		} finally{
			_server.removeClient(this);
			try {
				_socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void disconnect() {
		_connected = false;
	}

	public Socket getSocket() {
		return _socket;
	}
}
