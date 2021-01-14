package ap.project.chatmessenger;

import java.util.ArrayList;

public class Conversation {
	public ArrayList<Message> messages;
	static int lineCount = 0;
	
	Conversation(){
		this.messages = new ArrayList<Message>();
	}
	
	public void addMessage(Message m) {
		this.messages.add(m);
		incrementLineCount();
	}
	
	
	void incrementLineCount() {
	//there can be maximum 10 words per line
		
		for(Message m: messages) {
			int l = m.getText().length();
			for(int i = 0,j=0;i<l;i++) {//adds enter character if message longer than 10 words
				if(m.getText().charAt(i)==' ')
					j++;
				/*if(j>10) {
					if((i+1)!=l) {
						System.out.println(m.getText().length());
						String s1 = m.getText().substring(0, i);
						String s2 = m.getText().substring(i + 1, l-1);
						String s3 = s1.concat(s2);
						m.setText(s3);
						break;
					}
				}*/
				if(m.getText().charAt(i)=='\n' || m.getText().charAt(i)=='\0') //add to line count
					lineCount++;
			}	
		}
	}
	
	void displayMessages() {
		for(Message m : messages) {
			if(m.getSenderId() == 1)
				System.out.println("Armughan:");
			else if(m.getSenderId() == 2)
				System.out.println("Hyder:");
			else if(m.getSenderId() == 3)
				System.out.println("Bilal:");
			System.out.println(m.getText());
		}
		System.out.println("Total lines: "+ lineCount);
	}
	
	
	
}
