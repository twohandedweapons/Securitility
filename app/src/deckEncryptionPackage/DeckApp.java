package deckEncryptionPackage;

import java.io.File;

public class DeckApp {
	
	public static void main(String[] args)
	{
		DeckOperator dOp = new DeckOperator();
		Deck d = new Deck();
		
		d = dOp.locksmith();
		System.out.println("Locksmith execution finished.");
		
		DeckEncryptionService deckES = new DeckEncryptionService();
		File f = new File("fileToBeEncrypted.txt");
		if (!f.exists()) {
			System.out.println("ERROR: Failed to find fileToBeEncrypted");
		}
		
		if (deckES.ENCRYPT(f)) {
			System.out.println("main: File encrypted!!!");
			File eF = new File("vault/fileToBeEncrypted.txt.pwk");
			if (deckES.DECRYPT(eF, "decrypted")) {
				System.out.println("main: File decrypted!!!");
			} else {
				System.out.println("main: File could not be decrypted.");
			}
		} else {
			System.out.println("main: File could not be encrypted.");
		}
		
		//dOp.printDeck(d);
	}

}
