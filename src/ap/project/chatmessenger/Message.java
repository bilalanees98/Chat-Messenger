package ap.project.chatmessenger;

public class Message {
	int id;
	String text;
	int senderId;
	int receiverId;
	float time;
	int date;

	public Message(int id, String text, int si, int ri, float t, int d) {
		this.id = id;
		this.text = text;
		this.senderId = si;
		this.receiverId = ri;
		this.time = t;
		this.date = d;
	}

	public Message() {
		this(0, "", 0, 0, 0, 0);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int id) {
		this.senderId = id;
	}

	public int getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(int id) {
		this.receiverId = id;
	}

}
