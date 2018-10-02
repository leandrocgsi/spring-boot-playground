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
    public void convertToFloatTest() {
        assertEquals(0f, NumberConverter.CovertToFloat("A"), 0.0f);
        assertEquals(0f, NumberConverter.CovertToFloat(null), 0.0f);
        assertEquals(0f, NumberConverter.CovertToFloat(" "), 0.0f);
        assertEquals(0f, NumberConverter.CovertToFloat(""), 0.0f);
        assertEquals(5f, NumberConverter.CovertToFloat("5"), 0.0f);
        assertEquals(0f, NumberConverter.CovertToFloat("0"), 0.0f);
        assertEquals(-2f, NumberConverter.CovertToFloat("-2"), 0.0f);
        assertEquals(5.2f, NumberConverter.CovertToFloat("5,2"), 0.0f);
        assertEquals(5.2f, NumberConverter.CovertToFloat("5.2"), 0.0f);
    }

}
