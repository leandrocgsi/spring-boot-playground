package br.com.erudio.request.converters;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestNumberConverter {

    @Test
    public void isNumericTest() {
        assertFalse(NumberConverter.IsNumeric("A"));
        assertFalse(NumberConverter.IsNumeric(null));
        assertFalse(NumberConverter.IsNumeric(""));
        assertFalse(NumberConverter.IsNumeric(" "));
        assertTrue(NumberConverter.IsNumeric("5"));
        assertTrue(NumberConverter.IsNumeric("0"));
        assertTrue(NumberConverter.IsNumeric("-2"));
        assertTrue(NumberConverter.IsNumeric("5,2"));
        assertTrue(NumberConverter.IsNumeric("5.2"));
    }
    
    @Test
    public void convertToDoubleTest() {
       
        double expected = Double.parseDouble("5.2");
        
        assertEquals(0f, NumberConverter.CovertToDouble("A"), 0.0f);
        assertEquals(0f, NumberConverter.CovertToDouble(null), 0.0f);
        assertEquals(0f, NumberConverter.CovertToDouble(" "), 0.0f);
        assertEquals(0f, NumberConverter.CovertToDouble(""), 0.0f);
        assertEquals(5f, NumberConverter.CovertToDouble("5"), 0.0f);
        assertEquals(0f, NumberConverter.CovertToDouble("0"), 0.0f);
        assertEquals(-2f, NumberConverter.CovertToDouble("-2"), 0.0f);
        assertEquals(expected, NumberConverter.CovertToDouble("5,2"), 0.0f);
        assertEquals(expected, NumberConverter.CovertToDouble("5.2"), 0.0f);
    }

}
