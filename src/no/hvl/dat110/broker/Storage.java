package no.hvl.dat110.broker;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import no.hvl.dat110.common.TODO;
import no.hvl.dat110.messages.Message;
import no.hvl.dat110.common.Logger;
import no.hvl.dat110.messagetransport.Connection;

public class Storage {

	// data structure for managing subscriptions
	// maps from a topic to set of subscribed users
	protected ConcurrentHashMap<String, Set<String>> subscriptions;
	
	// data structure for managing currently connected clients
	// maps from user to corresponding client session object
	
	protected ConcurrentHashMap<String, ClientSession> clients;
					     // Key = disconnected user, Value = "Missed" messages while disconnected
	protected ConcurrentHashMap<String, Set<Message>> disconnectedUsers;

	public Storage() {
		subscriptions = new ConcurrentHashMap<String, Set<String>>();
		clients = new ConcurrentHashMap<String, ClientSession>();
		disconnectedUsers = new ConcurrentHashMap<String, Set<Message>>();
	}

	public Collection<ClientSession> getSessions() {
		return clients.values();
	}

	public Set<String> getTopics() {

		return subscriptions.keySet();

	}
	
	// ------------------- TASK E ---------------------
	
	//returns a set of disconnected users, for unit testing
	
	public Set<String> getDisconnectedUsers() {
		return disconnectedUsers.keySet();
	}
	
	
	//returns true if the user (param) is disconnected
	public boolean isDisconnected(String user) {
		return disconnectedUsers.containsKey(user);
	}
	
	//returns a set of missed buffered messages that was added while user (param) was disconnected
	public Set<Message> getMissedMessages(String user) {
		
		return disconnectedUsers.get(user);		
	}
	
	//buffers messages to the user (param) if this user is disconnected (present in the disconnectedUsers map)
	public boolean addMessageToDisconnectedUser(String user, Message message) {
		Set<Message> messages = disconnectedUsers.get(user);
		
		if (messages != null) {
		
			messages.add(message);
			disconnectedUsers.put(user, messages);
			return true;
		
		}
		return false;
	}

	// ------------------- TASK E ---------------------
	
	// get the session object for a given user
	// session object can be used to send a message to the user
	
	public ClientSession getSession(String user) {

		ClientSession session = clients.get(user);

		return session;
	}

	public Set<String> getSubscribers(String topic) {

		return (subscriptions.get(topic));

	}

	public void addClientSession(String user, Connection connection) {

		if (disconnectedUsers.containsKey(user)) {
			disconnectedUsers.remove(user);
		}
		
		ClientSession cs = new ClientSession(user, connection);
		
		
		
		clients.put(cs.getUser(), cs);

	}
		
	public void removeClientSession(String user) {
		
		disconnectedUsers.put(user, new HashSet<Message>());
		clients.remove(user);

	}
	
	public void createTopic(String topic) {
		
		subscriptions.put(topic, new HashSet<String>());
	
	}
	
	public void deleteTopic(String topic) {

		subscriptions.remove(topic);
	
	}

	public void addSubscriber(String user, String topic) {

	
		Set<String> subscribedUsers = subscriptions.get(topic);
		subscribedUsers.add(user);
		subscriptions.put(user, subscribedUsers);
	
	}
	
	public void removeSubscriber(String user, String topic) {


		Set<String> subscribedUsers = subscriptions.get(topic);
		subscribedUsers.remove(user);
		subscriptions.put(user, subscribedUsers);
	
	}
	
}
