package no.hvl.dat110.messages;

public class PublishMsg extends Message {
	
	// message sent from client to create publish a message on a topic 

	// TODO:
	// Implement object variables - a topic and a message is required

	// Constructor, get/set-methods, and toString method
	// as described in the project text
	
	
	private String user;
	private String topic;
	private String message;
	
	public PublishMsg() {
		// TODO Auto-generated constructor stub
	}
	
	public PublishMsg(String user, String topic, String message) {
		
		this.user = user;
		this.topic = topic;
		this.message = message;
		
	}
	
	public String getUser() {
		return user;
	}
	
	public String getTopic() {
		return topic;
	}
	
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {

		return "[Publish Message - user: " + user + ", topic: " + topic + ", message: " + message + "]\n";		
	}
	
	
}
