package no.hvl.dat159.roomcontrol;

public class Controller {

	double temp;
	boolean state;

	public Controller() {
		state = false;
	}
	
	public boolean setState(double temp) {
		if(temp < 17) {
			state = true;
		}
		else if(temp > 30) {
			state = false;
		}
		
		return state;
	}
	
	public void setTemp(String message) {
		temp = Double.parseDouble(message);
		setState(temp);
	}

	public boolean getState() {
		return state;
	}
	
}
