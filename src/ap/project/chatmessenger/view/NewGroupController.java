package ap.project.chatmessenger.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;

import ap.project.chatmessenger.MainApp;
import ap.project.chatmessenger.model.Person;
import ap.project.chatmessenger.Message;
import ap.project.chatmessenger.Conversation;

public class NewGroupController {
	@FXML
	private TextField groupName; //name of the group
	@FXML
	private ChoiceBox memberList; //also known as friendlist
	@FXML
	private TextField selectedFriends; //the friends separated with commas to be added to group
	@FXML
	private Button cancelButton;

	private MainApp mainApp;
	private Stage dialogStage;
	
	private ArrayList<String> groupChatNames = new ArrayList<String>(); //array to store the names to be added to group

	private void initialize() {
		// memberList
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		for (Person p : mainApp.getPersonData()) {
			memberList.getItems().add(p.getFullName());
		}

		// memberList.setItems(mainApp.getPersonData());
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	@FXML
	public void handleCreate() {
		String s ="";
		String s2 = selectedFriends.getText();
		for(int i=0;i<s2.length();i++) {
			if(s2.charAt(i)==',') {
				groupChatNames.add(s);
				s="";
			}
			else {
				s+=s2.charAt(i);
			}
		}
		System.out.println("List of friends to be added to group chat named '"+groupName.getText()+"':");
		System.out.println(groupChatNames);
		Stage stage = (Stage) groupName.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void handleCancel() {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void handleAddUser() {
		if (memberList.getSelectionModel().getSelectedItem() != null) {
			String friend = memberList.getSelectionModel().getSelectedItem().toString();
			if (selectedFriends.getText().equals("")) {
				selectedFriends.setText(friend + ",");
			} else {
				selectedFriends.setText(selectedFriends.getText() + friend + ",");
			}
		}
	}

}
