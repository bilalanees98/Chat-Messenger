package networking;

import java.net.*;
import java.io.*;
import javax.swing.*;

import ap.project.chatmessenger.MainApp;

import java.util.*;

public class ClientThread implements Runnable {

	// Globals
	Socket SOCK;
	public ObjectInputStream in;
	String[] currentUsers;
	boolean check = false;
	MainApp mainApp;

	// Constructor getting the socket
	public ClientThread(Socket X, MainApp mainApp) {
		this.SOCK = X;
		this.mainApp = mainApp;
	}

	@Override
	public void run() {
		System.out.println("client thread started");
		try {
			in = new ObjectInputStream(SOCK.getInputStream());
			CheckStream();
		} catch (Exception E) {
			// JOptionPane.showMessageDialog(null, E);
			E.printStackTrace();
			// System.out.println("socket closed - client thread closed");
		}

	}

	public void CheckStream() throws IOException, ClassNotFoundException {
		while (true) {
			if (!in.equals(null)) {
				String message = (String) in.readObject();
				System.out.println("client thread received: " + message);
				if (message.contains("@")) {
					int i = message.indexOf(":");
					String rcvrName = message.substring(1, i);
					mainApp.friendConvos.replace(rcvrName, mainApp.friendConvos.get(rcvrName) + message + "\n");
				}
			}
		}
	}

	public void RECEIVE() throws IOException, ClassNotFoundException {
		if (!in.equals(null)) {
			String message = (String) in.readObject();

			if (message.startsWith("!")) {
				String temp1 = message.substring(1);
				temp1 = temp1.replace("[", "");
				temp1 = temp1.replace("]", "");

				currentUsers = temp1.split(", ");
				Arrays.sort(currentUsers);
			}

			else if (message.startsWith("@EE@|")) {
				final String temp2 = message.substring(5);
			}

			else if (message.startsWith("@")) {
				final String temp3 = message.substring(1);
			}

		}
	}

	public void SEND(final String str) throws IOException {
		String writeStr;
		if (str.startsWith("@")) {
			writeStr = str;
		} else
			writeStr = "@EE@|" + Client.userName + ": " + str;

		Client.output.writeObject(writeStr);
		Client.output.flush();

	}

}
