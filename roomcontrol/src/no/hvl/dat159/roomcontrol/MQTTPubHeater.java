package no.hvl.dat159.roomcontrol;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTPubHeater implements Runnable {
	
	private String topic = "Heater";
	private int qos = 1;
	private String broker = "tcp://m21.cloudmqtt.com:17750";
	private String clientId = "MQTT_Heater";
	private String username = "dzwxyimr";
	private String password = "DsmqStR4oRAb";

	private MqttClient publisherClient;
	private Controller controller;
	
	private String status;
	private boolean state;

	public MQTTPubHeater(Controller controller) {
		this.controller = controller;
		state = false;
	}

	private void publish() throws MqttPersistenceException, MqttException, InterruptedException {

		for (int i = 0; i < 20; i++) {
			
			if(controller.getState() == true) {
				status = "On";
			}
			else {
				status = "Off";
			}
			
			if(state != controller.getState()) {
			System.out.println("Publishing message: " + status);

			MqttMessage message = new MqttMessage(status.getBytes());
			message.setQos(qos);

			publisherClient.publish(topic, message);
			
			state = controller.getState();
			}

			Thread.sleep(10000);
		}

	}

	private void connect() {

		MemoryPersistence persistence = new MemoryPersistence();

		try {
			publisherClient = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			connOpts.setUserName(username);
			connOpts.setPassword(password.toCharArray());
			System.out.println("Connecting to broker: " + broker);
			publisherClient.connect(connOpts);
			System.out.println("Connected");

		} catch (MqttException e) {
			System.out.println("reason " + e.getReasonCode());
			System.out.println("msg " + e.getMessage());
			System.out.println("loc " + e.getLocalizedMessage());
			System.out.println("cause " + e.getCause());
			System.out.println("excep " + e);
			e.printStackTrace();
		}
	}

	private void disconnect() throws MqttException {

		publisherClient.disconnect();

	}

	public void run() {

		try {

			System.out.println("Heater publisher running");

			connect();

			publish();

			disconnect();

			System.out.println("Heater publisher stopping");

		} catch (Exception ex) {
			System.out.println("Heater publisher: " + ex.getMessage());
			ex.printStackTrace();
		}

	}

}
