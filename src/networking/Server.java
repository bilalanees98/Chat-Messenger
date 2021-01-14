package networking;

import java.net.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.awt.BorderLayout;
import java.io.*;
import java.util.*;
import javax.swing.*;

import database.TestDB;

public class Server {
	private ServerSocket serverSocket;
	private Socket socket;
	public Hashtable<Socket, ObjectOutputStream> outputStreams;
	public Hashtable<String, ObjectOutputStream> clients;
	private TestDB db = new TestDB();
	Connection conn;

	// constructor
	public Server(int port) throws IOException {
		outputStreams = new Hashtable<Socket, ObjectOutputStream>();
		clients = new Hashtable<String, ObjectOutputStream>();
		try {
			conn = db.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println("Server could not connect to database");
		}
		serverSocket = new ServerSocket(port);
		showMessage("Waiting for clients at " + serverSocket);
	}

	// Waiting for clients to connect
	public void waitingForClients() throws IOException, ClassNotFoundException {

		while (true) {
			socket = serverSocket.accept();
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			String usernamePass = (String) in.readObject();
			int i = usernamePass.indexOf("@");
			String username = usernamePass.substring(0, i);
			String pass = usernamePass.substring(i + 1, usernamePass.length());
			ArrayList<String> checkUserInfo = new ArrayList<String>();
			boolean correctLogin = db.checkUser(username, pass, conn);
			if (correctLogin) {
				checkUserInfo.add("true");
				checkUserInfo.addAll(db.getOnlineFriends(conn, username));
			} else {
				checkUserInfo.add("false");
			}
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(checkUserInfo);
			if (correctLogin)
				new ServerThread(this, socket);
		}
	}

	// displaying message on Server Gui
	public void showMessage(final String message) {
		// TODO Auto-generated method stub
		System.out.println("Server showing message:" + message);
		System.out.println("total connected clients: " + clients.size());
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// displayWindow.append(message);
			}

		});
	}

	// Sending a message to all the available clients
	public void sendToAll(Object data) throws IOException {

		for (Enumeration<ObjectOutputStream> e = getOutputStreams(); e.hasMoreElements();) {
			// since we don't want server to remove one client and at the same time sending
			// message to it
			synchronized (outputStreams) {
				ObjectOutputStream tempOutput = e.nextElement();
				tempOutput.writeObject(data);
				tempOutput.flush();
			}
		}
	}

	// To get Output Stream of the available clients from the hash table
	private Enumeration<ObjectOutputStream> getOutputStreams() {
		// TODO Auto-generated method stub
		return outputStreams.elements();
	}

	// Sending private message
	public void sendPrivately(String username, String message) throws IOException {
		// TODO Auto-generated method stub
		if (clients.containsKey(username)) {
			ObjectOutputStream tempOutput = clients.get(username);
			tempOutput.writeObject(message);
			tempOutput.flush();
		}
		else {
			System.out.println(username + " is offline");
		}
	}

	// Removing the client from the client hash table
	public void removeClient(String username) throws IOException {

		synchronized (clients) {
			System.out.println("removing client with name: " + username);
			clients.remove(username);
			sendToAll("!" + clients.keySet());
		}
	}

	// Removing a connection from the outputStreams hash table and closing the
	// socket
	public void removeConnection(Socket socket, String username) throws IOException {

		synchronized (outputStreams) {
			System.out.println("removing socket for: " + username);
			outputStreams.remove(socket);
		}

		// Printing out the client along with the IP offline in the format of
		// ReetAwwsum(123, 12, 21, 21) is offline
		showMessage("\n" + username + "(" + socket.getInetAddress().getHostAddress() + ") is offline");

	}

}
