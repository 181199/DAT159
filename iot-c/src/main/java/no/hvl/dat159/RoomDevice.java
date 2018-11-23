package no.hvl.dat159;

public class RoomDevice {
	
	public static void main(String[] args) {
		
		Room room = new Room(20);
		Controller controller = new Controller();
		Heater heater = new Heater(room);
		Sensor sensor = new Sensor(room);
		
		try {
			
			Thread sense = new Thread(sensor);
			Thread control = new Thread(controller);
			Thread heat = new Thread(heater);		
			
			sense.start();
			control.start();
			heat.start();
			
			sense.join();
			control.join();			
			heat.join();
			
			
		} catch (Exception ex) {
			
			System.out.println("RoomDevice: " + ex.getMessage());
			ex.printStackTrace();
		}

	}

}
