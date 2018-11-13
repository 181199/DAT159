package no.hvl.dat159.roomcontrol;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTSubTemperature implements MqttCallback, Runnable {

	private String message;
	private Display display;
	private Controller controller;

	public MQTTSubTemperature(Controller controller) throws MqttException {

		String topic = "Temp";
		int qos = 1; // 1 - This client will acknowledge to the Device Gateway that messages are
						// received
		String broker = "tcp://m21.cloudmqtt.com:17750";
		String clientId = "MQTT_Temperature_SUB";
		String username = "dzwxyimr";
		String password = "DsmqStR4oRAb";

		display = new Display();
		this.controller = controller;

		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		connOpts.setUserName(username);
		connOpts.setPassword(password.toCharArray());

		System.out.println("Connecting to broker: " + broker);

		MqttClient client = new MqttClient(broker, clientId, new MemoryPersistence());
		client.setCallback(this);
		client.connect(connOpts);
		System.out.println("Connected");

		client.subscribe(topic, qos);
		System.out.println("Subscribed to topic " + topic);

	}

	/**
	 * @see MqttCallback#connectionLost(Throwable)
	 */
	public void connectionLost(Throwable cause) {
		System.out.println("Connection lost because: " + cause);
		System.exit(1);

	}

	/**
	 * @see MqttCallback#messageArrived(String, MqttMessage)
	 */
	public void messageArrived(String topic, MqttMessage message) throws Exception {

		String dismessage = String.format("[%s] %s", topic, new String(message.getPayload()));

		display.write(dismessage);

		this.setMessage(new String(message.getPayload()));
		
		controller.setTemp(new String(message.getPayload()));
	}
	
	/**
	 * @see MqttCallback#deliveryComplete(IMqttDeliveryToken)
	 */
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static void main(String args[]) throws MqttException {
		
		Controller controller = new Controller();

		new MQTTSubTemperature(controller);

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
