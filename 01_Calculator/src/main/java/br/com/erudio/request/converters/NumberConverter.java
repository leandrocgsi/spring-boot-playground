package br.com.erudio.request.converters;

public class NumberConverter {

    public static Double CovertToDouble(String strNumber) {
        if (strNumber == null) return 0d; 
        String number = strNumber.replaceAll(",", ".");
        if (IsNumeric(number)) return Double.parseDouble(number);
        return 0d;
    }

    public static boolean IsNumeric(String strNumber) {
        if (strNumber == null) return false; 
        String number = strNumber.replaceAll(",", ".");
        return number.matches("[-+]?[0-9]*\\.?[0-9]+");
    }
}
