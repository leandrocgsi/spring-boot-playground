package br.com.erudio.converter;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptTest {
	
    @Test
    public void bCryptTest() {
    	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
    	String result = encoder.encode("admin234");
    	System.out.println("My Hash " + result);
    	Assert.assertTrue(encoder.matches("admin234", result));
    }

}