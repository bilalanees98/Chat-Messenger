package networking;

import java.net.*;
import java.sql.SQLException;

import database.TestDB;

import java.io.*;

public class ServerThread extends Thread {

	private Server server;
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private String username;
	Object message;
	TestDB db;

	public ServerThread(Server server, Socket socket) throws IOException, ClassNotFoundException {
		// TODO Auto-generated constructor stub
		// db = new TestDB();
		System.out.println("server thread created");
		this.server = server;
		this.socket = socket;
		output = new ObjectOutputStream(this.socket.getOutputStream());
		output.flush();
		input = new ObjectInputStream(this.socket.getInputStream());

		username = (String) input.readObject();
		server.clients.put(username, output);
		server.outputStreams.put(socket, output);
		server.sendToAll("!" + server.clients.keySet());
		server.showMessage("\n" + username + " (" + socket.getInetAddress().getHostAddress() + ") is online");
		start();

	}

	@SuppressWarnings("deprecation")
	public void run() {

		try {
			// Thread will run until connections are present
			while (true) {
				try {
					message = input.readObject();
				} catch (Exception e) {
					stop();
				}
				if (message.toString().contains("@EE@")) {
					server.sendToAll(message);
					System.out.println("sent to all : " + message);
				} else {
					String formattedMsg = "@" + username + message.toString().substring(message.toString().indexOf(':'),
							message.toString().length());
					System.out.println("private message to: " + message.toString() + " : " + username);
					server.sendPrivately(message.toString().substring(1, message.toString().indexOf(':')),
							formattedMsg);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("----------------here is the problem-----------");
			e.printStackTrace();
		} finally {
//			try {
//				server.removeClient(username);
//				server.removeConnection(socket, username);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}

}
