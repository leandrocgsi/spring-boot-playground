package br.com.erudio.request.converters;

public class NumberConverter {

    public static Float CovertToFloat(String strNumber) {
        if (strNumber == null) return 0f; 
        String number = strNumber.replaceAll(",", ".");
        if (IsNumeric(number)) return Float.parseFloat(number);
        return 0f;
    }

    public static boolean IsNumeric(String strNumber) {
        if (strNumber == null) return false; 
        //Solution One: return StringUtils.isNumeric(strNumber);
        String number = strNumber.replaceAll(",", ".");
        return number.matches("[-+]?[0-9]*\\.?[0-9]+");
    }
}
