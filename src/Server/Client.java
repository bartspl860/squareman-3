package Server;

import Game.GameObject;
import Game.Player;
import Game.Point;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Client extends Thread{

	private Socket _socket;
	private Server _server;
	private Player thisPlayer;
	private Player anotherPlayer;
	private ReadFromServer readFromServer;
	private WriteToServer writeToServer;
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
				DataOutputStream out = new DataOutputStream(_socket.getOutputStream());
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
				readFromServer = new ReadFromServer(dis);
				writeToServer = new WriteToServer(out);
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

	// public static void main(String[] args) {
	// 	try {
	// 		Socket socket = new Socket("localhost", 6760);

	// 		Client client = new Client(socket, null);
	// 		client.start();
	// 	} catch (IOException e) {
	// 		e.printStackTrace();
	// 	}
	// }


	private class ReadFromServer implements Runnable{
		private DataInputStream dataInputStream;

		public ReadFromServer(DataInputStream dataInputStream) {
			this.dataInputStream = dataInputStream;
			System.out.println("ReadFromServer Runnable created");
		}

		@Override
		public void run() {
			try{
				while (true){
					if(anotherPlayer != null){
						Point point = new Point(dataInputStream.readInt(),dataInputStream.readInt());
						anotherPlayer.SetPosition(point);
					}
				}
			} catch (IOException e) {
				System.out.println("IOException from ReadFromServer run()");
			}
		}
	}

	private class WriteToServer implements Runnable{
		private DataOutputStream dataOutputStream;

		public WriteToServer(DataOutputStream dataOutputStream) {
			this.dataOutputStream = dataOutputStream;
			System.out.println("WriteToServer Runnable created");
		}

		@Override
		public void run() {
			try {
				while (true){
					if(thisPlayer != null){
						dataOutputStream.writeInt(thisPlayer.GetPosition().x);
						dataOutputStream.writeInt(thisPlayer.GetPosition().y);
						dataOutputStream.flush();
					}
					try{
						Thread.sleep(25);
					} catch (InterruptedException e){
						System.out.println("InterruptedException from WriteToServer run()");
					}
				}
			} catch (IOException e) {
				System.out.println("IOException from WriteToServer()");
			}
		}
	}

}

