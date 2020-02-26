package no.hvl.dat110.messages;

public class CreateTopicMsg extends Message {
	
	// message sent from client to create topic on the broker
	
	// TODO: 
	// Implement object variables - a topic is required
	
	// Constructor, get/set-methods, and toString method
    // as described in the project text	
	
	private String user;
	private String topic;
	
	
	public CreateTopicMsg() {}
	
	public CreateTopicMsg(String user, String topic) {
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
	
}
