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

    private Float CovertToDecimal(String number) {
        if (IsNumeric(number)) return Float.parseFloat(number);
        return 0f;
    }

    private boolean IsNumeric(String strNumber) {
        return StringUtils.isNumeric(strNumber);
    }
}