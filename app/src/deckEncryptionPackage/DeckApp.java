package deckEncryptionPackage;

public class DeckApp {
	
	public static void main(String[] args)
	{
		DeckOperator dOp = new DeckOperator();
		Deck d = new Deck();
		
		d = dOp.locksmith();
		
		dOp.printDeck(d);
	}

}
