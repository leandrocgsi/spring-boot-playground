package br.com.erudio.controllers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.erudio.exception.MathOperationUnsuportedException;
import br.com.erudio.math.SimpleMath;

@RestController
public class MathController {

    private SimpleMath math = new SimpleMath();

    @RequestMapping(value="/sum/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Float sum(@PathVariable("numberOne") String numberOne, @PathVariable("numberTwo") String numberTwo) {
        
        if (!IsNumeric(numberOne) || !IsNumeric(numberTwo)) {
            throw new MathOperationUnsuportedException("Please set a numeric value");
        }
        
        Float sum = math.sum(CovertToDecimal(numberOne), CovertToDecimal(numberTwo));
        return sum;
    }
    
    @RequestMapping(value="/subtraction/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Float subtraction(@PathVariable("numberOne") String numberOne, @PathVariable("numberTwo") String numberTwo) {
        
        if (!IsNumeric(numberOne) || !IsNumeric(numberTwo)) {
            throw new MathOperationUnsuportedException("Please set a numeric value");
        }
        
        Float subtraction = math.subtraction(CovertToDecimal(numberOne), CovertToDecimal(numberTwo));
        return subtraction;
    }
    
    @RequestMapping(value="/division/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Float division(@PathVariable("numberOne") String numberOne, @PathVariable("numberTwo") String numberTwo) {
        
        if (!IsNumeric(numberOne) || !IsNumeric(numberTwo)) {
            throw new MathOperationUnsuportedException("Please set a numeric value");
        }
        
        Float division = math.division(CovertToDecimal(numberOne), CovertToDecimal(numberTwo));
        return division;
    }
    
    @RequestMapping(value="/multiplication/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Float multiplication(@PathVariable("numberOne") String numberOne, @PathVariable("numberTwo") String numberTwo) {
        
        if (!IsNumeric(numberOne) || !IsNumeric(numberTwo)) {
            throw new MathOperationUnsuportedException("Please set a numeric value");
        }
        
        Float multiplication = math.multiplication(CovertToDecimal(numberOne), CovertToDecimal(numberTwo));
        return multiplication;
    }
    
    @RequestMapping(value="/mean/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Float mean(@PathVariable("numberOne") String numberOne, @PathVariable("numberTwo") String numberTwo) {
        
        if (!IsNumeric(numberOne) || !IsNumeric(numberTwo)) {
            throw new MathOperationUnsuportedException("Please set a numeric value");
        }
        
        Float mean = math.mean(CovertToDecimal(numberOne), CovertToDecimal(numberTwo));
        return mean;
    }

    @RequestMapping(value="/squareRoot/{number}", method=RequestMethod.GET)
    public Float squareRoot(@PathVariable("number") String number) {
        
        if (!IsNumeric(number)) {
            throw new MathOperationUnsuportedException("Please set a numeric value");
        }
        
        Float squareRoot = math.squareRoot(CovertToDecimal(number));
        return squareRoot;
    }

    private Float CovertToDecimal(String strNumber) {
        String number = strNumber.replaceAll(",", ".");
        if (IsNumeric(number)) return Float.parseFloat(number);
        return 0f;
    }

    private boolean IsNumeric(String strNumber) {
        //Solution One: return StringUtils.isNumeric(strNumber);
        String number = strNumber.replaceAll(",", ".");
        return number.matches("[-+]?[0-9]*\\.?[0-9]+");
    }
}