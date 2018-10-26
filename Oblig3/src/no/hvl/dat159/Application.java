package no.hvl.dat159;

import java.util.ArrayList;
import java.util.List;

public class Application {

	private static UTXO utxo = new UTXO();
	private static int blocknumber = 0;

	public static void main(String[] args) throws Exception {

		/*
		 * In this assignment, we are going to look at how to represent and record
		 * monetary transactions. We will use Bitcoin as the basis for the assignment,
		 * but there will be some simplifications.
		 * 
		 * We are skipping the whole blockchain this time, and instead focus on the
		 * transaction details, the UTXOs and how money movements are represented.
		 * 
		 * (If you want to, you can of course extend the assignment by collecting the
		 * individual transactions into blocks, create a Merkle tree for the block
		 * header, validate, mine and add the block to a blockchain.)
		 * 
		 */

		// 0. To get started, we need a few (single address) Wallets. Create 2 wallets.
		// Think of one of them as the "miner" (the one collecting "block rewards").

		Wallet wallet = new Wallet("Address1", utxo);
		Wallet miner = new Wallet("Address2", utxo);

		// 1. The first "block" (= round of transactions) contains only a coinbase
		// transaction. Create a coinbase transaction that adds a certain
		// amount to the "miner"'s address. Update the UTXO-set (add only).

		CoinbaseTx firstBlock = new CoinbaseTx("Genesis", 10, miner.getAddress());
		utxo.addOutputFrom(firstBlock);

		// 2. The second "block" contains two transactions, the mandatory coinbase
		// transaction and a regular transaction. The regular transaction shall
		// send ~20% of the money from the "miner"'s address to the other address.
		CoinbaseTx secondBlock = new CoinbaseTx("Block 2", 10, miner.getAddress());
		utxo.addOutputFrom(secondBlock);
		Transaction regularSecondBlock = miner.createTransaction(Math.round(miner.getBalance() * 0.2D),
				wallet.getAddress());

		// Validate the regular transaction created by the "miner"'s wallet:
		// - All the content must be valid (not null++)!!!
		if (!regularSecondBlock.isValid())
			throw new Exception("Block is not valid.");
		
		// - All the inputs are unspent and belongs to the sender
		// - There are no repeating inputs!!!
		// - All the outputs must have a value > 0
		// - The sum of inputs equals the sum of outputs
		// - The transaction is correctly signed by the sender
		// - The transaction hash is correct	
		if(!validation(utxo, miner, regularSecondBlock))
			throw new Exception("Block is not valid.");

		// Update the UTXO-set (both add and remove).
		utxo.addAndRemoveOutputsFrom(regularSecondBlock);
		
		print(firstBlock, regularSecondBlock);

		// 3. Do the same once more. Now, the "miner"'s address should have two or more
		// unspent outputs (depending on the strategy for choosing inputs) with a
		// total of 2.6 * block reward, and the other address should have 0.4 ...
		CoinbaseTx thirdBlock = new CoinbaseTx("Block 3", 10, miner.getAddress());
		utxo.addOutputFrom(thirdBlock);
		Transaction regularThirdBlock = miner.createTransaction(Math.round(miner.getBalance() * 0.2D),
				wallet.getAddress());


		// Validate the regular transaction ...
		if (!regularThirdBlock.isValid())
			throw new Exception("Block is not valid.");
		
		if(!validation(utxo, miner, regularThirdBlock))
			throw new Exception("Block is not valid.");

		// Update the UTXO-set ...
		utxo.addAndRemoveOutputsFrom(regularThirdBlock);
		
		print(firstBlock, regularThirdBlock);

		// 4. Make a nice print-out of all that has happened, as well as the end status.
		//
		// for each of the "block"s (rounds), print
		// "block" number
		// the coinbase transaction
		// hash, message
		// output
		// the regular transaction(s), if any
		// hash
		// inputs
		// outputs
		// End status: the set of unspent outputs
		// End status: for each of the wallets, print
		// wallet id, address, balance
        
        utxo.printUTXO();
        System.out.println();
        System.out.println(wallet.toString());
        System.out.println(miner.toString());
	}
	
	public static void print(CoinbaseTx coinbaseTx, Transaction transactionTx) {
		blocknumber++;
		System.out.println("Block number " + blocknumber + ":");
        System.out.println(coinbaseTx.toString());
        System.out.println(transactionTx.toString());
        System.out.println();
	}
	
	public static boolean validation(UTXO utxo, Wallet miner, Transaction transaction) {
		// - All the inputs are unspent and belongs to the sender
        for (Input input : transaction.getInputs()) {
            if (!utxo.getUTXOMap().containsKey(input))
                return false;
            if (!utxo.getUTXOMap().get(input).getAddress().equals(miner.getAddress()))
                return false;
        }

        // - There are no repeating inputs!!!
        List<String> prevTxHashes = new ArrayList<>();
        for (Input input : transaction.getInputs()) {
            if (prevTxHashes.contains(input.getPrevTxHash()))
                return false;
            prevTxHashes.add(input.getPrevTxHash());
        }

        // - All the outputs must have a value > 0
        for (Output output : transaction.getOutputs()) {
            if (output.getValue() <= 0)
                return false;
        }

        // - The sum of inputs equals the sum of outputs
        long inputSum = 0;
        for (Input input : transaction.getInputs())
            inputSum += utxo.getUTXOMap().get(input).getValue();

        long outputSum = 0;
        for (Output output : transaction.getOutputs())
            outputSum += output.getValue();

        if (inputSum != outputSum)
            return false;

        // - The transaction is correctly signed by the sender
        if(!DSAUtil.verifyWithDSA(miner.getPublicKey(), transaction.getMessage(), transaction.getSignature()))
            return false;

        // - The transaction hash is correct
        String newTxHash = HashUtil.base64Encode(HashUtil.sha256Hash(transaction.getMessage()));
        if (!transaction.getTxHash().equals(newTxHash))
        		return false;
        
        return true;
	}
}
