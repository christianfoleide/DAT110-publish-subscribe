package no.hvl.dat110.iotsystem;

import no.hvl.dat110.client.Client;
import no.hvl.dat110.messages.Message;
import no.hvl.dat110.messages.PublishMsg;
import no.hvl.dat110.common.TODO;

public class DisplayDevice {
	
	private static final int COUNT = 10;
		
	public static void main (String[] args) {
		
		System.out.println("Display starting ...");
		
		Client displayClient = new Client("Display", Common.BROKERHOST, Common.BROKERPORT);
		
		displayClient.connect();
		
		displayClient.createTopic(Common.TEMPTOPIC);		
		displayClient.subscribe(Common.TEMPTOPIC);
	
		for (int i = 0; i < COUNT; i++) {
			
			Message received = displayClient.receive();
			
			System.out.println(received.toString());
			
		}
		
		displayClient.unsubscribe(Common.TEMPTOPIC);		
		displayClient.disconnect();
		
		System.out.println("Display stopping ... ");
		
	}
}
