package no.hvl.dat159;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class Transaction {

	// Simplified compared to Bitcoin
	private List<Input> inputs = new ArrayList<>();
	private List<Output> outputs = new ArrayList<>();

	// If we make the assumption that all the inputs belong to the
	// same key, we can have one signature for the entire transaction,
	// and not one for each input. This simplifies things a lot
	// (more than you think)!
	private PublicKey senderPublicKey;
	private byte[] signature;

	private String txHash;

	public Transaction(PublicKey senderPublicKey) {
		this.senderPublicKey = senderPublicKey;
	}

	public void addInput(Input input) {
		inputs.add(input);
	}

	public void addOutput(Output output) {
		outputs.add(output);
	}

	@Override
	public String toString() {
		return "Transaction: " + txHash + "\nInputs: " + inputsToString() + "Outputs: " + outputsToString();
	}

	public void signTxUsing(PrivateKey privateKey) {
		signature = DSAUtil.signWithDSA(privateKey, getMessage());
	}

	public void calculateTxHash() {
		byte[] sha256 = HashUtil.sha256Hash(inputsToString() + outputsToString());
		txHash = HashUtil.base64Encode(sha256);
	}

	public boolean isValid() {
		if (inputs == null || outputs == null || senderPublicKey == null || signature == null || txHash == null)
			return false;
		
		return true;
	}

	private String inputsToString() {
		String inputString = "";
		for (Input input : inputs) {
			inputString += input.toString() + "\n";
		}
		return inputString;
	}

	private String outputsToString() {
		String outputString = "";
		for (Output output : outputs) {
			outputString += output.toString() + "\n";
		}
		return outputString;
	}

	public String getMessage() {
		return inputsToString() + outputsToString();
	}

	public List<Input> getInputs() {
		return inputs;
	}

	public List<Output> getOutputs() {
		return outputs;
	}

	public PublicKey getSenderPublicKey() {
		return senderPublicKey;
	}

	public byte[] getSignature() {
		return signature;
	}

	public String getTxHash() {
		return txHash;
	}

}
