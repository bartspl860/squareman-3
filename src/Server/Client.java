package Server;

import java.io.*;
import java.net.*;

public class Client extends Thread{

	private Socket _socket;
	private Server _server;

	public Client(Socket soc, Server serv){
		this._socket = soc;
		this._server = serv;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
	}
	
	public void disconnect(){

	}
}
