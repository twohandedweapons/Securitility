/* -----------------
 * DECKOPERATOR.JAVA
 * -----------------
 * This is an object operations file for the Deck.java object.
 * Ideally, this should be packaged together with the object
 * file in deckEncryptionPackage. 
 * 
 * Written by arcticprogrammer
 * Nanyang Polytechnic 140514M
 * 2862abc566cba008dbd95bf70dda04f8
 * 
 * version 0.2.528
 *  */

package deckEncryptionPackage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DeckOperator {
	private final static String CHARACTERS = "1234567890-=qwertyuiop[]asdfghjkl;'zxcvbnm,./!@#$%^&*()_+QWERTYUIOP{}|ASDFGHJKL:ZXCVBNM<>?";
	private final static SecureRandom RAND = new SecureRandom();

	
	public static Deck locksmith()
	{
		String mIp = getMacIpAddress();
		String dealer = genDealer(mIp);
		String keyString = genKeyString();
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0 ; i < dealer.length() ; i++)
			sb.append((char)(dealer.charAt(i) 
					^ keyString.charAt(i % keyString.length())));
		
		ArrayList<Integer> deckState = shuffle(dealer);
		Map<Integer, String> mapping = genMap(dealer);

		//String finalKey = sb.toString();
		//System.out.println("finalKey Size: " + finalKey.length());
		
		Deck d = new Deck(mIp, dealer, deckState, mapping);
		
		if (saveDeckOnSystem(d))
			System.out.println("Successfully saved deck on system.");
		else System.out.println("Failed to save deck on system.");
		
		return d;
		
	}
	
	public static boolean saveDeckOnSystem(Deck d)
	{
		FileOutputStream fOut = null;
		File outFile;
		String content = printDeckMap(d);
		
		//if the directory does not exist, create it
		File dir = new File("deckMap");
		if (!dir.exists()) {
		    System.out.println("creating directory: " + dir.getName());
		    boolean result = false;
		
		    try{
		        dir.mkdir();
		        result = true;
		    } 
		    catch(SecurityException se){
		        se.printStackTrace();
		    }        
		    if(result) {    
		        System.out.println("Directory /deckMap created");  
		    } else {
		    	System.out.println("Directory /deckMap already exists.");
		    }
		}
		
		//write actual deckmap.pwk file
		try {
			//declare deckMap file
			outFile = new File("deckMap/deckMap.pwk");
			
			//if file doesn't exist, create the file
			if (!outFile.exists()) {
				outFile.createNewFile();
			} else {
				System.out.println("deckMap.pwk ALREADY EXISTS! ABORTING SAVE");
				return false;
			}
			
			//declare file writer
			fOut = new FileOutputStream(outFile);
			
			//get byte content of output data
			byte[] byteContent = content.getBytes();
			
			//write to file
			fOut.write(byteContent);
			
			//flush and close
			fOut.flush();
			fOut.close();
			
			System.out.println("Saved map to deckMap.pwk");
			
			return true;
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fOut != null) {
					fOut.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	private static String getMacIpAddress()
	{
		//dpc com.mkyong
		InetAddress ip;
		String ipAddress= "";
		String macAddress= "";
		
		try {
			//get host ip
			ip = InetAddress.getLocalHost();
			
			//get network interface of the host by ip
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress();
			
			//build the host mac address
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++)
			{
				//sb.append(mac[i]);
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			}
			macAddress = sb.toString();
			ipAddress = ip.toString();
			
			//System.out.println(ipAddress);
			//System.out.println(macAddress);
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		String macIpAddress = ipAddress + macAddress;
		
		macIpAddress = addressCleanUp(macIpAddress);
		
		return macIpAddress;
		
	}
	
	private static String genDealer(String fullHand)
	{		
		//formulate mutation
		StringBuilder mutation = new StringBuilder();
		while (mutation.length() < 12)
		{
			int index = (int) (RAND.nextFloat() * fullHand.length());
			mutation.append(fullHand.charAt(index));
		}
		String apReturn = mutation.toString();
		return apReturn;
	}
	
	private static String genKeyString()
	{
		Integer keyArray[] = new Integer[3];
		
		for (int i = 0 ; i < keyArray.length ; i++)
		{
			do {
				keyArray[i] = RAND.nextInt(9999);
			} while (keyArray[i] < 1000);
		}
		
		String keyString = "";
		for (int i = 0 ; i < keyArray.length ; i++)
			keyString += keyArray[i];
		
		return keyString;
	}
	
	private static ArrayList<Integer> shuffle(String dealer)
	{
		ArrayList<Integer> cardCount = new ArrayList<Integer>();
		ArrayList<Integer> shuffledDeck = new ArrayList<Integer>();
		String localDealer = dealer;
		
		//fill cardCount<Integer> with 1 to 52
		for (Integer i = 1 ; i <= 52 ; i++)
			cardCount.add(i);
		
		do {
			//new random seed
			RAND.setSeed(localDealer.getBytes());
			
			//determine which card to take
			Integer card = cardCount.get(RAND.nextInt(cardCount.size()));
			
			//remove that card from cardCount, add it to shuffledDeck
			cardCount.remove(card);
			shuffledDeck.add(card);
			
			localDealer = rotateString(localDealer, true);
			
		} while (!cardCount.isEmpty());
		
		return shuffledDeck;
		
	}
	
	private static Map<Integer, String> genMap(String dealer)
	{
		
		Map<Integer, String> dictionary = new HashMap<Integer, String>();
		String localDealer = dealer;
		char[] cArray = CHARACTERS.toCharArray();
		
		int card = 1;
		do {
			String characterSet = "";
			int setSize = 52;
			do { 
				localDealer = rotateString(localDealer, false);
				
				//new random seed
				RAND.setSeed(localDealer.getBytes());
				
				//copy selected character from cArray to characterSet
				characterSet += cArray[RAND.nextInt(cArray.length)];
				
				//reduce setSize 1
				setSize--;
			} while (setSize != 0);
			
			dictionary.put(card, characterSet);
			card++;
		} while (card <= 52);
		
		return dictionary;
		
	}
	

	//QOL functions
	private static String addressCleanUp(String s)
	{
		s = s.replace(".", "");
		s = s.replace("-", "");
		s = s.replace("/", "");
		
		return s;
	}
	
	public static void printDeck(Deck d)
	{
		//print macip and dealer
		System.out.println("MacIp: " + d.getMacIp());
		System.out.println("Dealer: " + d.getDealer());
		
		//print deckstate
		System.out.print("Deckstate: ");
		for (int i = 0 ; i < d.getDeckState().size() ; i++)
		{
			if (i % 13 == 0)
				System.out.println();
			System.out.print("(" + d.getDeckState().get(i) + ")");
		}
		
		System.out.println();
		
		//print mapping
		System.out.println("Mapping: ");
		//get the set of entries
		Set set = d.getMapping().entrySet();
		
		//get iterator
		Iterator i = set.iterator();
		
		//display
		while (i.hasNext())
		{
			Map.Entry<Integer, String> me = (Map.Entry)i.next();
			System.out.print(me.getKey() + ": ");
			System.out.println(me.getValue());
			
		}
		
	}
	
	public static String printDeckMap(Deck d)
	{
		String output = "";
		Set set = d.getMapping().entrySet();
		Iterator i = set.iterator();
		
		while (i.hasNext())
		{
			Map.Entry<Integer, String> mapEntry = (Map.Entry)i.next();
			output += mapEntry.getValue() + "\n";
		}
		
		return output;
	}
	
	private static String rotateString(String s, boolean d)
	{
		char t;
		char[] cA = s.toCharArray();
		
		if (d == false)
		{
			//left rotate
			t = cA[0];
			for (int i = 0 ; i < cA.length ; i++)
			{
				if (i == cA.length - 1)
				{
					cA[i] = t;
					break;
				}
				cA[i] = cA[i+1];
			}
		} else
		{
			//right rotate
			t = cA[cA.length - 1];
			for (int i = cA.length - 1 ; i >= 0 ; i--)
			{
				if (i == 0)
				{
					cA[i] = t;
					break;
				}
				cA[i] = cA[i-1];
			}
		}
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0 ; i < cA.length ; i++)
			sb.append(cA[i]);
		
		String rotatedString = sb.toString();
		return rotatedString;
	}
	
}
