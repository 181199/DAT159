package no.hvl.dat159.roomcontrol.tests;

import no.hvl.dat159.roomcontrol.Controller;
import no.hvl.dat159.roomcontrol.Heating;
import no.hvl.dat159.roomcontrol.Room;
import no.hvl.dat159.roomcontrol.TemperatureSensor;

public class RoomTester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Room room = new Room(0);

		TemperatureSensor sensor = new TemperatureSensor(room);
		
		Heating heater = new Heating(room);
		
		Controller controller = new Controller();
		
		try {
			
			Thread t1 = new Thread((Runnable) sensor);
			
			t1.start();	
			
			t1.join();
			
		} catch (Exception ex) {
			System.out.println("TemperatureSensor: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

}
