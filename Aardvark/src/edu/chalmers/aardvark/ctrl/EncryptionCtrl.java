package edu.chalmers.aardvark.ctrl;

import edu.chalmers.aardvark.model.Chat;
import android.util.Log;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class EncryptionCtrl {
    private static EncryptionCtrl instance;
    private KeyPair keys;

    private EncryptionCtrl() {
	Log.i("CLASS", this.toString() + " STARTED");

    }

    public static EncryptionCtrl getInstance() {
	if (instance == null) {
	    instance = new EncryptionCtrl();
	}
	return instance;
    }

    public void generateKeys(){
	try {
	    KeyPairGenerator keyGenerator;

	    keyGenerator = KeyPairGenerator.getInstance("RSA");

	    SecureRandom random =
		SecureRandom.getInstance("SHA1PRNG", "SUN");

	    keyGenerator.initialize(1024, random);

	    KeyPair keys = keyGenerator.generateKeyPair();
	} catch (NoSuchAlgorithmException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	catch (NoSuchProviderException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public RSAPrivateKey getPrivateKey(){
	return (RSAPrivateKey)keys.getPrivate();
    }

    public RSAPublicKey getPublicKey(){
	return (RSAPublicKey)keys.getPublic();
    }

    public void encryptChat(Chat chat, boolean enabled){}

    public String encryptMessage(String message){
	try {
	    // Create the cipher
	    Cipher rsaCipher;

	    rsaCipher = Cipher.getInstance("RSA");


	    // Initialize the cipher for encryption
	    rsaCipher.init(Cipher.ENCRYPT_MODE, (Key)getPublicKey());

	    byte[] unencryptedMessage = message.getBytes();

	    // Encrypt the cleartext
	    byte[] encryptedMessage = rsaCipher.doFinal(unencryptedMessage);

	    message = encryptedMessage.toString();

	    return message;
	} catch (NoSuchAlgorithmException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (NoSuchPaddingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (InvalidKeyException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IllegalBlockSizeException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (BadPaddingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return message;
    }

    public String decryptMessage(String message){
	try {
	    Cipher rsaCipher;

	    rsaCipher = Cipher.getInstance("RSA");


	    // Initialize the same cipher for decryption
	    rsaCipher.init(Cipher.DECRYPT_MODE, (Key)getPrivateKey());

	    // Decrypt the ciphertext
	    byte[] decryptedMessage = rsaCipher.doFinal(message.getBytes());

	    message = decryptedMessage.toString();

	    return message;
	} catch (NoSuchAlgorithmException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (NoSuchPaddingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (InvalidKeyException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IllegalBlockSizeException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (BadPaddingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return message;
    }
}
