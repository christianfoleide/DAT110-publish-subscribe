package no.hvl.dat110.iotsystem;

import no.hvl.dat110.client.Client;
import no.hvl.dat110.common.TODO;

public class TemperatureDevice {

	private static final int COUNT = 10;

	public static void main(String[] args) {

		TemperatureSensor sn = new TemperatureSensor();

		Client sensorClient = new Client("Sensor", Common.BROKERHOST, Common.BROKERPORT);
		sensorClient.connect();
		
		for (int i = 0; i < COUNT; i++) {
			
			sensorClient.publish(Common.TEMPTOPIC, "" + sn.read());	
		}
		
		sensorClient.disconnect();
		
		System.out.println("Temperature device stopping ... ");

	}
}
