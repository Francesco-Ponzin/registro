package it.engim.fp.registro;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;

public class Test {

	
	private static String sha256(String base) {
	    try{
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] hash = digest.digest(base.getBytes("UTF-8"));
	        StringBuffer hexString = new StringBuffer();

	        for (int i = 0; i < hash.length; i++) {
	            String hex = Integer.toHexString(0xff & hash[i]);
	            if(hex.length() == 1) hexString.append('0');
	            hexString.append(hex);
	        }

	        return hexString.toString();
	    } catch(Exception ex){
	       throw new RuntimeException(ex);
	    }
	}
	
	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		Random RANDOM = new SecureRandom();
		String salt = Integer.toString(RANDOM.nextInt());
		String password = "password";
		System.out.println(password);
		System.out.println(salt);

		System.out.println(salt + password);
		System.out.println(sha256(salt + password));
	}

}
