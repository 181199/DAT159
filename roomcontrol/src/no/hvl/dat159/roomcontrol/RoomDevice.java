package no.hvl.dat159.roomcontrol;

import org.eclipse.paho.client.mqttv3.MqttException;

public class RoomDevice {

	public static void main(String[] args) throws MqttException {
		
		Room room = new Room(20);
		
		TemperatureSensor sensor = new TemperatureSensor(room);
		
		MQTTPubTemperature sensorpub = new MQTTPubTemperature(sensor);
		
		Heating heater = new Heating(room);
		
		Controller controller = new Controller();
		
		MQTTSubTemperature sensorsub = new MQTTSubTemperature(controller);
		
		MQTTPubHeater heaterpub = new MQTTPubHeater(controller);
		
		MQTTSubHeater heatersub = new MQTTSubHeater(heater);
		
		try {
			
			Thread temppublisher = new Thread(sensorpub);
			Thread heatsub = new Thread(heatersub);
			Thread heatpub = new Thread(heaterpub);
			
			// TODO: add heating subscriber running in its own thread
			temppublisher.start();
			heatpub.start();
			heatsub.start();
			
			temppublisher.join();
			
			heatpub.join();
		
			heatsub.join();
			
			
		} catch (Exception ex) {
			
			System.out.println("RoomDevice: " + ex.getMessage());
			ex.printStackTrace();
		}

	}

}
