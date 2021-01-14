package ap.project.chatmessenger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Scanner;

import ap.project.chatmessenger.model.Person;
import ap.project.chatmessenger.view.ChatUsersController;
import ap.project.chatmessenger.view.NewGroupController;
import ap.project.chatmessenger.view.UserLoginController;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import networking.*;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;

	public Hashtable<String, String> friendConvos;
	public static int messageId = 1;
	int currentUserId = 0; // ****The User Logged in from this instance*****//
	ChatUsersController controller;
	UserLoginController controller1;
	public ArrayList<String> loginDetails = new ArrayList<String>();
	public Client client;
	public Conversation conversation;

	private ObservableList<Person> personData = FXCollections.observableArrayList();

	public void setPersonData(ArrayList<String> arr) {
		for (int i = 0; i < arr.size(); i++) {
			personData.add(new Person(arr.get(i)));
		}
	}

	public void initializeConvos(ArrayList<String> arr) {
		for (int i = 0; i < arr.size(); i++) {
			friendConvos.put(arr.get(i), "");
		}
	}

	// public void getMessagesFromFile() {
	// File fp = new File("dummymessages.txt");
	// try {
	// Scanner fr = new Scanner(fp);
	// while (fr.hasNext()) {
	// String temp = fr.nextLine();
	// int i = 0;
	// while (temp.charAt(i) != '|') {
	// i++;
	// }
	// i++;
	// String temp2 = "";
	// temp2 += temp.charAt(i);
	// temp = temp.substring(0, i - 1);
	// conversation.addMessage(new Message(messageId, temp, 0,
	// Integer.valueOf(temp2), 0, 0));
	// }
	// fr.close();
	// for (Message m : messages) {
	// System.out.println(m.text);
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * Constructor
	 */
	public MainApp() {
		conversation = new Conversation();
		client = new Client();
		friendConvos = new Hashtable<String, String>();
	}

	/**
	 * Returns the data as an observable list of Persons.
	 * 
	 * @return
	 */
	public ObservableList<Person> getPersonData() {
		return personData;
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("ChatMessenger");

		initRootLayout();
		primaryStage.setResizable(false);
		showUserLogin();
		if (!primaryStage.isShowing()) {
			initRootLayout();
			showChatUsers();
		}

	}

	/**
	 * Initializes the root layout.
	 */
	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Shows the person overview inside the root layout.
	 */

	public void showUserLogin() {
		try {
			// Load User login screen
			FXMLLoader loader1 = new FXMLLoader();
			loader1.setLocation(MainApp.class.getResource("view/UserLogin.fxml"));
			AnchorPane login = (AnchorPane) loader1.load();
			// set login screen into center of root layout
			rootLayout.setCenter(login);
			// give Login controller access to main app
			controller1 = loader1.getController();
			controller1.setMainApp(this);

			controller1.okButton.setOnAction(e -> controller1.handleOk());
			System.out.println(loginDetails);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showChatUsers() {
		try {

			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/ChatUsers.fxml"));
			AnchorPane chatUsers = (AnchorPane) loader.load();

			// Set person overview into the center of root layout.
			rootLayout.setCenter(chatUsers);

			// Give the controller access to the main app.
			controller = loader.getController();
			displayMessages();
			client.setUserController(controller);
			// Initialize current user's name
			controller.currentUserLabel.setText(client.userName);
			controller.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createNewGroupDialog() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/NewGroupDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Create new group");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Give the controller access to the main app.
			NewGroupController controller = loader.getController();
			controller.setMainApp(this);
			controller.setDialogStage(dialogStage);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showConvo() {
		// System.out.println(controller.fullNameLabel.getText() + "---------------");
		controller.messageLabel.setText(friendConvos.get(controller.fullNameLabel.getText()));
	}

	public void displayMessages() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				Runnable updater = new Runnable() {

					@Override
					public void run() {
						// incrementCount();
						showConvo();
					}
				};

				while (true) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException ex) {
					}
					Platform.runLater(updater);
				}
			}

		});
		// don't let thread prevent JVM shutdown
		thread.setDaemon(true);
		thread.start();
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) throws Exception {
		launch(args);
	}
}