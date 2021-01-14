package networking;
import javax.swing.*;
import javax.swing.border.*;

import ap.project.chatmessenger.MainApp;
//import ap.project.chatmessenger.model.Person;
//import ap.project.chatmessenger.view.ChatUsersController;
//import ap.project.chatmessenger.view.UserLoginController;
//import javafx.application.Application;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.BorderPane;
//import javafx.stage.Stage;
import ap.project.chatmessenger.view.ChatUsersController;

import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;


public class Client{
	// Globals
	public static ClientThread clientThread;
	public static String userName = "Anonymous";
	public Socket SOCK;
	public static ObjectOutputStream output;
	public static ArrayList<String> friends = new ArrayList<String>();
	public static boolean loggedIn = false;
	public static ChatUsersController chatController;
	public static String message;
	
	public void Connect(Socket s,MainApp mainApp) {

		try {
			SOCK = s;
			output = new ObjectOutputStream(SOCK.getOutputStream());
			clientThread = new ClientThread(SOCK,mainApp);
			
			Thread X = new Thread(clientThread);
			X.start();
			
		} catch (Exception x) {
			System.out.println("Server Not Responding");
			System.out.println(x);
			// JOptionPane.showMessageDialog(null, "Server Not Responding");
			System.exit(0);
		}
	}
	public void writeLogin(String un, String pass) {
		this.userName = un;
		// sending UserName
		try {
			output.writeObject(userName);
			output.flush();
		} catch (IOException ioException) {
			System.out.println("Error - UserName not Sent!");
		}

	}
	public void setUserController(ChatUsersController c) {
		System.out.println("set controller");
		chatController = c;
		clientThread.check = true;
	}
	public void writeMessage(Object ob, String rcvrName) throws IOException{
			clientThread.SEND("@" + rcvrName + ":" + ob);
//		while (true) {
//			System.out.println("do you want to send anything?(enter 'quit' to exit)");
//			// Scanner in = new Scanner(System.in);
//			String ans = in.nextLine();
//			if (!ans.equals("quit")) {
//				if (!ans.equals("no")) {
//					System.out.println("do you wnat to broadcast?");
//					ans = in.nextLine();
//					if (ans.equals("yes")) {
//						System.out.println("type your message: ");
//						String msg = in.nextLine();
//						clientThread.SEND("@EE@" + msg);
//					} else {
//						System.out.println("who do you want to send it to?");
//						String rcvrName = in.nextLine();
//						System.out.println("type your message: ");
//						String msg = in.nextLine();
//						clientThread.SEND("@" + rcvrName + ":" + msg);
//					}
//				}
//			}else if(ans.equals("quit")) {
//				//write quit functionality
//			}
//		}

	}

}