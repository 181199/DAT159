package no.hvl.dat159;

import java.io.IOException;

public class Heater implements Runnable {

	private Room room;
	private boolean state;
	private HeaterClient hc;
	private Heat heat;

	public Heater(Room room) {
		this.room = room;
		hc = new HeaterClient();
		heat = new Heat();
		state = false;
	}

	public void write(boolean newvalue) {
		room.actuate(newvalue);
	}

	public boolean getState() {
		return state;
	}

	@Override
	public void run() {
		while (true) {
			try {
				state = Boolean.parseBoolean(hc.getState());
				if (state == false) {
					write(false);
					heat.setState(false);
				} else if (state == true) {
					write(true);
					heat.setState(true);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
