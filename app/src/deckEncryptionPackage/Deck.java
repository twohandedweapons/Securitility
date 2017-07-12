/* ---------
 * DECK.JAVA
 * ---------
 * This is an object file for the Deck object. Its
 * associated operations file is DeckOperations.java.
 * Ideally, this should be packaged together with the
 * object access file in deckEncryptionPackage. 
 * 
 * Written by arcticprogrammer
 * Nanyang Polytechnic 140514M
 * ITNOA,TLOTB
 *  */

package deckEncryptionPackage;

import java.util.ArrayList;
import java.util.Map;

public class Deck {
		
	private String macIp;
	private String dealer;
	private ArrayList<Integer> deckState;
	private Map<Integer, String> mapping;
	
	//constructor
	public Deck()
	{ }
	
	public Deck(String macIp, String dealer, ArrayList<Integer> deckState, Map<Integer,String> mapping)
	{
		this.macIp = macIp;
		this.dealer = dealer;
		this.deckState = deckState;
		this.mapping = mapping;
	}

	public String getMacIp() {
		return macIp;
	}

	public void setMacIp(String macIp) {
		this.macIp = macIp;
	}

	public String getDealer() {
		return dealer;
	}

	public void setDealer(String dealer) {
		this.dealer = dealer;
	}

	public ArrayList<Integer> getDeckState() {
		return deckState;
	}

	public void setDeckState(ArrayList<Integer> deckState) {
		this.deckState = deckState;
	}

	public Map<Integer, String> getMapping() {
		return mapping;
	}

	public void setMapping(Map<Integer, String> mapping) {
		this.mapping = mapping;
	}
	
	
	

}
