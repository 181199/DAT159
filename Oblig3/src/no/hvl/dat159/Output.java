package no.hvl.dat159;

public class Output {

    //Simplified compared to Bitcoin - The address should be a script
	private long value;
	private String address;
	
	public Output(long value, String address) {
	    this.value = value;
	    this.address = address;
	}

	@Override
	public String toString() {
        //TODO For screen output
        return "Value: " + getValue() + ", Address: " + getAddress();
	}

	public long getValue() {
		return value;
	}

	public String getAddress() {
		return address;
	}
	
}
