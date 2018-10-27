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

	public boolean isValid(UTXO utxo, Wallet miner) {
		// - All the content must be valid (not null++)!!!
		if (inputs == null || outputs == null || senderPublicKey == null || signature == null || txHash == null)
			return false;
		
		// - All the inputs are unspent and belongs to the sender
        for (Input input : getInputs()) {
            if (!utxo.getUTXOMap().containsKey(input))
                return false;
            if (!utxo.getUTXOMap().get(input).getAddress().equals(miner.getAddress()))
                return false;
        }

        // - There are no repeating inputs!!!
        List<String> prevTxHashes = new ArrayList<>();
        for (Input input : getInputs()) {
            if (prevTxHashes.contains(input.getPrevTxHash()))
                return false;
            prevTxHashes.add(input.getPrevTxHash());
        }

        // - All the outputs must have a value > 0
        for (Output output : getOutputs()) {
            if (output.getValue() <= 0)
                return false;
        }

        // - The sum of inputs equals the sum of outputs
        long inputSum = 0;
        for (Input input : getInputs())
            inputSum += utxo.getUTXOMap().get(input).getValue();

        long outputSum = 0;
        for (Output output : getOutputs())
            outputSum += output.getValue();

        if (inputSum != outputSum)
            return false;

        // - The transaction is correctly signed by the sender
        if(!DSAUtil.verifyWithDSA(miner.getPublicKey(), getMessage(), getSignature()))
            return false;

        // - The transaction hash is correct
        String newTxHash = HashUtil.base64Encode(HashUtil.sha256Hash(getMessage()));
        if (!getTxHash().equals(newTxHash))
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
