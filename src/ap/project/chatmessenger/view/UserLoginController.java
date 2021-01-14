package ap.project.chatmessenger.view;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import ap.project.chatmessenger.MainApp;
import ap.project.chatmessenger.model.Person;
import database.TestDB;
import networking.*;

public class UserLoginController {
	@FXML
	private TextField usernameField;
	@FXML
	private TextField passwordField;

	@FXML
	public Button exitButton;
	@FXML
	public Button okButton;
	
	private Person person;
	private boolean okClicked = false;
	private MainApp mainApp;
	public static Socket SOCK;
	
	private void initialize() {
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	public Boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	public void handleOk() {
		Stage stage = (Stage) okButton.getScene().getWindow();
		if (isInputValid()) {
			// Search the database for username and password matching
			System.out.println(usernameField.getText() + passwordField.getText());
			okClicked = true;
			//mainApp.setCurrentUserId(usernameField.getText());
			try {
				final int port = 5555;
				SOCK = new Socket(InetAddress.getLocalHost(), port);
				ObjectOutputStream output = new ObjectOutputStream(SOCK.getOutputStream());
				output.writeObject(usernameField.getText() + "@" + passwordField.getText());
				ObjectInputStream input = new ObjectInputStream(SOCK.getInputStream());
				ArrayList<String> userInfo = new ArrayList<String>((ArrayList<String>)input.readObject());
				if (userInfo.get(0).equals("true")) {
					userInfo.remove(0);
					mainApp.setPersonData(userInfo);
					mainApp.initializeConvos(userInfo);
					mainApp.client.Connect(SOCK,mainApp);
					mainApp.client.writeLogin(usernameField.getText(), passwordField.getText());
					
					stage.setHeight(538);
					stage.setWidth(784);
					stage.setX(300);

					mainApp.showChatUsers();
				} else {
					// Show the error message.
					Alert alert = new Alert(AlertType.ERROR);
					alert.initOwner(stage);
					alert.setTitle("Error");
					alert.setHeaderText("Enter correct details");
					alert.setContentText("Username/password wrong");

					alert.showAndWait();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(ClassNotFoundException e) {
				e.printStackTrace();//graceful handling krni hai
			}
		}
		// return loginDetails;
	}

	public void handleEnter() {
		passwordField.setOnAction(e->handleOk());
	}
	
	@FXML
	private void handleExit() {
		Stage stage = (Stage) exitButton.getScene().getWindow();
		stage.close();
	}

	private boolean isInputValid() {
		String errorMessage = "";

		if (usernameField.getText() == null || passwordField.getText().length() == 0) {
			errorMessage += "Username/Password can't be empty!\n";
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initModality(Modality.WINDOW_MODAL);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}

}
