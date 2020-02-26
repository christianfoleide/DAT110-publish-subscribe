package no.hvl.dat110.messages;

public class SubscribeMsg extends Message {

	
	
	// message sent from client to subscribe on a topic 

	// TODO:
	// Implement object variables - a topic is required

	// Constructor, get/set-methods, and toString method
	// as described in the project text
	
	private String user;
	private String topic;
	
	public SubscribeMsg() {}
	
	public SubscribeMsg(String user, String topic) {
		
		this.user = user;
		this.topic = topic;
		
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
	
	@Override
	public String toString() {
		
		return "[Subscribe Message - user: " + user + ", topic: " + topic + "]\n";
	}
	
}
