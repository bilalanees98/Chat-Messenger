package ap.project.chatmessenger.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ArrayList;

import ap.project.chatmessenger.MainApp;
import ap.project.chatmessenger.model.Person;
import ap.project.chatmessenger.Message;
import ap.project.chatmessenger.Conversation;

public class ChatUsersController {
	@FXML
	private TableView<Person> personTable; // for all friend list
	@FXML
	private TableColumn<Person, String> friendsColumn; // for all friends
	@FXML
	private TableView<Person> personTable2; // for online friend list
	@FXML
	private TableColumn<Person, String> onlineColumn; // for online friends

	@FXML
	public TextField sendField; // text box
	@FXML
	public Label currentUserLabel; // sender/current user
	@FXML
	public Label fullNameLabel; // receiver name
	@FXML
	public Label messageLabel; // message
	@FXML
	private ImageView videoCall;
	@FXML
	private ImageView voiceCall;
	@FXML
	private ImageView newGroupButton;
	// Reference to the main application.
	private MainApp mainApp;

	/**
	 * The constructor. The constructor is called before the initialize() method.
	 */
	public ChatUsersController() {
	}

	/**
	 * Initializes the controller class. This method is automatically called after
	 * the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		// Initialize the person table with the one column.
		friendsColumn.setCellValueFactory(cellData -> cellData.getValue().fullNameProperty());

		// Clear person details.
		showPersonDetails(null);
		showMessages(null);

		// Listen for selection changes and show the person details when changed.
		// for all friends
		personTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showPersonDetails(newValue));
		personTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showMessages(newValue));
		// for online friends
		personTable2.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showPersonDetails(newValue));
		personTable2.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showMessages(newValue));
	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	@FXML
	public void handleSend() {
		// insert your code before clearing text field(done below)
		try {
			mainApp.client.writeMessage(sendField.getText(), fullNameLabel.getText());
			write(messageLabel.getText() + currentUserLabel.getText() + ":" + sendField.getText() + "\n");
			// messageLabel.setText(messageLabel.getText() + currentUserLabel.getText()+":"+
			// sendField.getText() + "\n");
			// mainApp.friendConvos.replace(fullNameLabel.getText(),
			// messageLabel.getText());
			sendField.setText("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("could not send");
		}
	}
	@FXML
	public void handleEnter() {
		sendField.setOnAction(e -> handleSend());
		// handleSend();
	}

	@FXML
	public void handleNewGroup() {
		mainApp.createNewGroupDialog();
	}

	public void write(String msg) {
		// System.out.println("in write");
		messageLabel.setText("\n" + msg);
		mainApp.friendConvos.replace(fullNameLabel.getText(), msg);
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

		// Add observable list data to the table
		//for all friends
		personTable.setItems(mainApp.getPersonData());
		//for online friends
		personTable2.setItems(mainApp.getPersonData());/////////////////////-----------change this function to get online friends only
	}

	private void showPersonDetails(Person person) {
		if (person != null) {
			// Fill the labels with info from the person object.
			fullNameLabel.setText(person.getFullName());

		} else {
			// Person is null, remove all the text.
			fullNameLabel.setText("");

		}
	}

	private void showMessages(Person person) {
		String text = "";
		if (person != null) {
			if (mainApp.friendConvos.containsKey(person.getFullName())) {
				messageLabel.setText(mainApp.friendConvos.get(person.getFullName()));
			} else {
				System.out.println("not a friend");
			}
		} else {
			messageLabel.setText("");
		}
	}
}