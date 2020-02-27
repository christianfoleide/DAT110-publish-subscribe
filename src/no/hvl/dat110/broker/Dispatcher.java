package no.hvl.dat110.broker;

import java.util.Set;
import java.util.Collection;

import no.hvl.dat110.common.TODO;
import no.hvl.dat110.common.Logger;
import no.hvl.dat110.common.Stopable;
import no.hvl.dat110.messages.*;
import no.hvl.dat110.messagetransport.Connection;

public class Dispatcher extends Stopable {

	private Storage storage;

	public Dispatcher(Storage storage) {
		super("Dispatcher");
		this.storage = storage;

	}

	@Override
	public void doProcess() {

		Collection<ClientSession> clients = storage.getSessions();

		Logger.lg(".");
		for (ClientSession client : clients) {

			Message msg = null;

			if (client.hasData()) {
				msg = client.receive();
			}

			// a message was received
			if (msg != null) {
				dispatch(client, msg);
			}
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void dispatch(ClientSession client, Message msg) {

		MessageType type = msg.getType();

		// invoke the appropriate handler method
		switch (type) {

		case DISCONNECT:
			onDisconnect((DisconnectMsg) msg);
			break;

		case CREATETOPIC:
			onCreateTopic((CreateTopicMsg) msg);
			break;

		case DELETETOPIC:
			onDeleteTopic((DeleteTopicMsg) msg);
			break;

		case SUBSCRIBE:
			onSubscribe((SubscribeMsg) msg);
			break;

		case UNSUBSCRIBE:
			onUnsubscribe((UnsubscribeMsg) msg);
			break;

		case PUBLISH:
			onPublish((PublishMsg) msg);
			break;

		default:
			Logger.log("broker dispatch - unhandled message type");
			break;

		}
	}

	// called from Broker after having established the underlying connection
	public void onConnect(ConnectMsg msg, Connection connection) {

		String user = msg.getUser();

		Logger.log("onConnect:" + msg.toString());
		
		//if user is present in the disconnectedUsers-map, send the buffered messages and remove the user
		//from the map.
				
		
		
		if (storage.isDisconnected(user)) {
					
			Set<Message> missedMessages = storage.getMissedMessages(user);
			
			storage.addClientSession(user, connection);
			ClientSession prevUser = storage.getSession(user);
			
			missedMessages.forEach( message -> {
				try {
					prevUser.send(message);
					Thread.sleep(100);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			});
			
					
		} else {
			storage.addClientSession(user, connection);
		}
				
	}

	// called by dispatch upon receiving a disconnect message
	public void onDisconnect(DisconnectMsg msg) {

		String user = msg.getUser();

		Logger.log("onDisconnect:" + msg.toString());
		 
		//adds user to map of disconnected users upon disconnection
		storage.removeClientSession(user);

	}

	public void onCreateTopic(CreateTopicMsg msg) {

		Logger.log("onCreateTopic:" + msg.toString());

		storage.createTopic(msg.getTopic());
		

	}

	public void onDeleteTopic(DeleteTopicMsg msg) {

		Logger.log("onDeleteTopic:" + msg.toString());
		
		
		storage.deleteTopic(msg.getTopic());
		
	}

	public void onSubscribe(SubscribeMsg msg) {

		Logger.log("onSubscribe:" + msg.toString());
		
		storage.addSubscriber(msg.getUser(), msg.getTopic());
		
	}

	public void onUnsubscribe(UnsubscribeMsg msg) {

		Logger.log("onUnsubscribe:" + msg.toString());
	
		storage.removeSubscriber(msg.getUser(), msg.getTopic());
		
	
	}

	public void onPublish(PublishMsg msg) {

		Logger.log("onPublish:" + msg.toString());
		
		Set<String> subscribers = storage.getSubscribers(msg.getTopic());
		
		subscribers.forEach( sub -> {
			
			//if the subscriber is disconnected, add the message (param) to his buffered ("missing") messages set
			if (storage.isDisconnected(sub)) {
				storage.addMessageToDisconnectedUser(sub, msg);
				Logger.log("User " + sub + " is disconnected, adding message to his buffered messages.");
			} else {
			
				ClientSession subSession = storage.getSession(sub);
				subSession.send(msg);
							
			}
			
		});
		
	}
}
