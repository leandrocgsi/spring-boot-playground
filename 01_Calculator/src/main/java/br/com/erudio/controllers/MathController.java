package br.com.erudio.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.erudio.exception.UnsuportedMathOperationException;
import br.com.erudio.math.SimpleMath;
import br.com.erudio.request.converters.NumberConverter;

@RestController
public class MathController {

    private SimpleMath math = new SimpleMath();
    

    @RequestMapping(value="/sum/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Float sum(@PathVariable("numberOne") String numberOne, @PathVariable("numberTwo") String numberTwo) {
        
        if (!NumberConverter.IsNumeric(numberOne) || !NumberConverter.IsNumeric(numberTwo)) {
            throw new UnsuportedMathOperationException("Please set a numeric value");
        }
        
        Float sum = math.sum(NumberConverter.CovertToFloat(numberOne), NumberConverter.CovertToFloat(numberTwo));
        return sum;
    }
    
    @RequestMapping(value="/subtraction/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Float subtraction(@PathVariable("numberOne") String numberOne, @PathVariable("numberTwo") String numberTwo) {
        
        if (!NumberConverter.IsNumeric(numberOne) || !NumberConverter.IsNumeric(numberTwo)) {
            throw new UnsuportedMathOperationException("Please set a numeric value");
        }
        
        Float subtraction = math.subtraction(NumberConverter.CovertToFloat(numberOne), NumberConverter.CovertToFloat(numberTwo));
        return subtraction;
    }
    
    @RequestMapping(value="/division/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Float division(@PathVariable("numberOne") String numberOne, @PathVariable("numberTwo") String numberTwo) {
        
        if (!NumberConverter.IsNumeric(numberOne) || !NumberConverter.IsNumeric(numberTwo)) {
            throw new UnsuportedMathOperationException("Please set a numeric value");
        }
        
        Float division = math.division(NumberConverter.CovertToFloat(numberOne), NumberConverter.CovertToFloat(numberTwo));
        return division;
    }
    
    @RequestMapping(value="/multiplication/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Float multiplication(@PathVariable("numberOne") String numberOne, @PathVariable("numberTwo") String numberTwo) {
        
        if (!NumberConverter.IsNumeric(numberOne) || !NumberConverter.IsNumeric(numberTwo)) {
            throw new UnsuportedMathOperationException("Please set a numeric value");
        }
        
        Float multiplication = math.multiplication(NumberConverter.CovertToFloat(numberOne), NumberConverter.CovertToFloat(numberTwo));
        return multiplication;
    }
    
    @RequestMapping(value="/mean/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Float mean(@PathVariable("numberOne") String numberOne, @PathVariable("numberTwo") String numberTwo) {
        
        if (!NumberConverter.IsNumeric(numberOne) || !NumberConverter.IsNumeric(numberTwo)) {
            throw new UnsuportedMathOperationException("Please set a numeric value");
        }
        
        Float mean = math.mean(NumberConverter.CovertToFloat(numberOne), NumberConverter.CovertToFloat(numberTwo));
        return mean;
    }

    @RequestMapping(value="/squareRoot/{number}", method=RequestMethod.GET)
    public Float squareRoot(@PathVariable("number") String number) {
        
        if (!NumberConverter.IsNumeric(number)) {
            throw new UnsuportedMathOperationException("Please set a numeric value");
        }
        
        Float squareRoot = math.squareRoot(NumberConverter.CovertToFloat(number));
        return squareRoot;
    }

}