package br.com.erudio.converter;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class FooTest {
	
    @Test
    public void parseEntityToVOTest() {
    	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
    	String result = encoder.encode("admin123");
    	Assert.assertTrue(encoder.matches("admin123", result));
    }

}