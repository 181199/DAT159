package no.hvl.dat159;

import java.io.IOException;

public class Sensor implements Runnable {
	
	private Room room;
	private TempClient tc;

	public Sensor(Room room) {
		this.room = room;
		tc = new TempClient();
	}

	public double read() {
		return room.sense();
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				double temp = read();
				tc.publishTemp(temp);
				System.out.printf("[TEMPERATURE]: PUT temperature. New temp: %,.2f%n", temp);
				Thread.sleep(1000);	// 1 second
			} catch (IOException | InterruptedException e) {
				System.err.println("API not available.");
				System.exit(1);
			}
		}
	}

}
