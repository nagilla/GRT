package com.grt.util;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

public class PasswordUtil {
	private static final Logger logger = Logger.getLogger(PasswordUtil.class);
	private static final char[] PASSWORD = "hfnpv9q86tq349tysdjkfgsuyrfbw78"
			.toCharArray();
	private static final byte[] SALT = { (byte) 0xde, (byte) 0x33, (byte) 0x10,
			(byte) 0x12, (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12, };

	public static void main(String[] args) throws Exception {

		PasswordUtil security = new PasswordUtil();

		String originalPassword = (args.length == 0) ? "zsjYeGD0KqK6gF9c9GL8aA==" : args[0];

		System.out.println("Original password: " + originalPassword);
		//String encryptedPassword = security.encrypt(originalPassword);
		String encryptedPassword ="zsjYeGD0KqK6gF9c9GL8aA==";
		System.out.println("Encrypted password: " + encryptedPassword);
		String decryptedPassword = security.decrypt(encryptedPassword);
		System.out.println("Decrypted password: " + decryptedPassword);
	}

	private String encrypt(String property) throws GeneralSecurityException {
		SecretKeyFactory keyFactory = SecretKeyFactory
				.getInstance("PBEWithMD5AndDES");
		SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
		Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
		pbeCipher
				.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
		return base64Encode(pbeCipher.doFinal(property.getBytes()));
	}

	private static String base64Encode(byte[] bytes) {
		return new String(Base64.encodeBase64(bytes));

	}

	public static String decrypt(String property) {
		try {
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance("PBEWithMD5AndDES");

			SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));

			Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");

			pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT,
					20));

			return new String(pbeCipher.doFinal(base64Decode(property)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static byte[] base64Decode(String property) throws IOException {
		return Base64.decodeBase64(property);
	}

}
