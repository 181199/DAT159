package no.hvl.dat159;

import java.io.IOException;

public class Controller implements Runnable {

	double temp;
	boolean state;
	private HeaterClient hc;
	private TempClient tc;

	public Controller() {
		state = false;
		hc = new HeaterClient();
		tc = new TempClient();
	}

	public boolean getState() {
		return state;
	}

	@Override
	public void run() {
		while (true) {
			try {

				boolean check = state;
				temp = Double.parseDouble(tc.getTemp());

				if (temp < 17) {
					state = true;
				} else if (temp > 30) {
					state = false;
				}
 
				if (getState() != check) {
					hc.publishState(getState());
					System.out.println("[HEATER] GET heat. New state: " + (getState() ? "ON" : "OFF"));
				}
				
				check = getState();
			} catch (IOException e) {
				System.err.println("API not available.");
				System.exit(1);
			}
		}

	}

}
