/* -----------------
 * DECKENCRYPTIONSERVICE.JAVA
 * -----------------
 * This is the service file associated with the Deck
 * encryption methods in deckEncryptionPackage. This
 * class will handle encryption, decryption and file
 * handling and storage.
 * 
 * Written by arcticprogrammer
 * Nanyang Polytechnic 140514M
 * ITNOA,TLOTB
 * 
 * version 0.2.501
 *  */

package deckEncryptionPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DeckEncryptionService {
	
	/*---ENC FUNCTIONS---*/
	public static boolean ENCRYPT(File inFile)
	{
		String keyString = formulateKeyString();
		String fName = inFile.getName(); //get the name of the input file
		
		
		//xor file to key
		byte[] outFileBytes = xorFileToKey(inFile, keyString);
		
		//save file
		checkDirectoryExistence(true, null);
		String destinationFileName = "vault/" + fName + ".pwk";
		if (convertToFile(destinationFileName, outFileBytes)) {
			System.out.println("File successfully encrypted!");
//			if (inFile.delete()) {
//				System.out.println("inFile deleted from source.");
//			} else {
//				System.out.println("Failed to delete inFile from source. However, encrypted file has been saved to the vault, so you may safely delete the inFile.");
//			}
			return true;
		} else {
			System.out.println("Unable to write the file! ( ENCRYPT )");
			return false;
		}
		
	}
	
	public static boolean DECRYPT(File inFile)
	{
		// << should be the same as encrypt
		String keyString = formulateKeyString();
		String fName = inFile.getName();
		
		
		//xor file to key
		byte[] outFileBytes = xorFileToKey(inFile, keyString);
		
		//save file
		//Scanner sc = new Scanner(System.in);
		System.out.println("Store to directory: /decrypted");
		String dirName = "decrypted";
		//String dirName = sc.nextLine();
		//sc.close();
		checkDirectoryExistence(false, dirName);
		
		fName = fName.replace(".pwk", "");
		String destinationFileName = dirName + "/" + fName;
		
		if (convertToFile(destinationFileName, outFileBytes)) {
			System.out.println("File successfully decrypted!");
//			if (inFile.delete()) {
//				System.out.println("inFile deleted from source.");
//			} else {
//				System.out.println("Failed to delete inFile from source. However, encrypted file has been saved to the vault, so you may safely delete the inFile.");
//			}
			return true;
		} else {
			System.out.println("Unable to write the file! ( DECRYPT )");
			return false;
		}
	}
	
	
	
	
	
	
	/*---QOL FUNCTIONS---*/
	private static byte[] convertToByteArray(File f)
	{
		InputStream is = null;
		
		//assign file to inputstream
		try {
			is = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			System.out.println("Failed trycatch assignment of File to InputStream");
			e.printStackTrace();
		}
		
		
		//---EI1---//
		byte[] fData = new byte[(int) f.length()];
		try {
			is.read(fData);
		} catch (IOException e) {
			System.out.println("Failed to read File f into fData in convertToByteArray");
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return fData;
	}
	
	/*---KEY FUNCTIONS---*/
	private static boolean checkDirectoryExistence(boolean b, String s)
	{
		//check if necessary directories exist
		//if they don't, create them
		
		if (b == true) //true -> check for vault
		{
			File vaultDir = new File("vault");
			if (!vaultDir.exists()) {
				System.out.println("Creating directory: " + vaultDir.getName());
				boolean result = false;
				
				try {
					vaultDir.mkdir();
					result = true;
				} catch (SecurityException se) {
					se.printStackTrace();
				}
				
				if (result) {
					System.out.println("Directory /vault created");
				} else {
					System.out.println("Failed to create directory /vault");
				}
			} else return true;
		} 
		else //false -> check for user-defined dir
		{
			//System.out.println("Store to directory: /" + s);
			String dirName = s;
			
			File newDir = new File(dirName);
			if (!newDir.exists()) {
				System.out.println("Creating directory: " + newDir.getName());
				boolean result = false;
				
				try {
					newDir.mkdir();
					result = true;
				} catch (SecurityException se) {
					se.printStackTrace();
				}
				
				if (result) {
					System.out.println("Directory /" + newDir.getName() + " created");
				} else {
					System.out.println("Failed to create directory /" + newDir.getName());
				}
			} else return true;
		}
		
		System.out.println("CheckVaultLocation: execution complete.");
		return true;
	}
	
	private static boolean convertToFile(String fileName, byte[] outFileBytes)
	{
		//save file
		File outFile = null;
		FileOutputStream fOut = null;
		
		try {
			//declare outFile file
			outFile = new File(fileName);
			
			//create file if it doesn't exist (should not exist in this case)
			if (!outFile.exists()) {
				System.out.println(">> Creating file " + outFile.getName());
				outFile.createNewFile();
			}
			
			//write byte content to outFile
			fOut = new FileOutputStream(outFile);
			fOut.write(outFileBytes);
			
			//flush and close, if successful
			fOut.flush();
			fOut.close();
			
			return true;
			
		} catch (IOException e) {
			System.out.println("Triggered IOException in fOut of convertToFile");
			e.printStackTrace();
			return false;
		} 
	}
	
	private static byte[] xorFileToKey(File f, String k)
	{
		//convert file and keystring to byte array
		byte[] inFileBytes = convertToByteArray(f);
		byte[] keyStringBytes = k.getBytes();
		
		//xor with filebytes and keystringbytes
		int i = 0;
		byte[] outFileBytes = new byte[inFileBytes.length];
		
		while (i < inFileBytes.length) {
			int keyByte = 0;
			while (keyByte < keyStringBytes.length){
				if (i == inFileBytes.length) {
					break;
				} else {
					outFileBytes[i] = (byte) (inFileBytes[i] ^ keyStringBytes[keyByte]);
					keyByte++;
					i++;
				}
			}
		}
		
		//return xor'd byte[]
		return outFileBytes;
	}
	
	//get deckmap from system
	private static Map<Integer, String> getDeckMapFromSystem()
	{
		Map<Integer, String> deckMap = new HashMap<Integer, String>();
		
		//try to get file
		FileReader fr = null;
		try {
			fr = new FileReader("/deckMap/deckMap.pwk");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			System.out.println("ERROR: deckMap.pwk not found on system.");
		}
		
		BufferedReader br = new BufferedReader(fr);
		
		try {
			for (int i = 0 ; i < 52 ; i++) {
				String line = br.readLine();
				deckMap.put(i+1, line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return deckMap;
	}
	
	//get key from server [TODO]
	private static ArrayList<Integer> getKeyFromServer()
	{
		//--INCOMPLETE--//
		//testing code
		ArrayList<Integer> testList = new ArrayList<Integer>();
		
		for (int i = 0 ; i < 52 ; i++)
			testList.add(i + 1);
		
		return testList;
	}
	
	//formulate keystring
	private static String formulateKeyString()
	{
		Map<Integer, String> mapping = getDeckMapFromSystem();
		ArrayList<Integer> serverKey = getKeyFromServer();
		String localKey = "";
		
		for (int i = 0 ; i < serverKey.size() ; i++)
		{
			localKey += mapping.get(serverKey.get(i));
		}
			
		return localKey;
	}
	
}
