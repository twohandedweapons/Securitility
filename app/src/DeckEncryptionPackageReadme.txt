[DECK ENCRYPTION SERVICE]
How to use:
1. In order to use the DeckEncryptionService, a Deck object must first
be created and filled.
	1.1. To create a Deck object, simple call for one.
		eg. Deck d = new Deck();
	1.2. To fill the Deck object, create a DeckOperator and call
	the locksmith function.
		eg. DeckOperator dOp = new DeckOperator();
		    d = dOp.locksmith();
	1.3. You now have a filled Deck object.

2. Create an instance of the DeckEncryptionService. This instance will be
used to perform the service's encryption/decryption functions.
	eg. DeckEncryptionService ES = new DeckEncryptionService;

==For Encrypting/Decrypting a File==
3. The ENCRYPT(File) function takes in a file, and stores it
into the vault directory that is automatically created upon
its first run. This will return a boolean.

4. The DECRYPT(File, String) function takes in a file from
the vault, as well as a user-defined string indicating where
to place the decrypted file in the system. This will return a 
boolean.

==For Encrypting/Decrypting using PIN==
5. The ENCRYPT_WITH_PIN(String, String, String) function takes
first the user's PIN, and then the DATA, and then the user-defined
NAME of the data, in that order. It then saves the data as a
separate .pwk file based on that name, in the pinVault. This pinVault
is automatically generated upon its first run. This will return
a boolean.

6. The DECRYPT_WITH_PIN(String, String) takes in the user's PIN,
and then the NAME of the data as stored in the pinVault. This 
function will return the decrypted string of whatever is stored
in the .pwk file.