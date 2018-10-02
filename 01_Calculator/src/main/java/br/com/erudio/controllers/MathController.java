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
    public Double sum(@PathVariable("numberOne") String numberOne, @PathVariable("numberTwo") String numberTwo) {
        
        if (!NumberConverter.IsNumeric(numberOne) || !NumberConverter.IsNumeric(numberTwo)) {
            throw new UnsuportedMathOperationException("Please set a numeric value");
        }
        
        Double sum = math.sum(NumberConverter.CovertToDouble(numberOne), NumberConverter.CovertToDouble(numberTwo));
        return sum;
    }
    
    @RequestMapping(value="/subtraction/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Double subtraction(@PathVariable("numberOne") String numberOne, @PathVariable("numberTwo") String numberTwo) {
        
        if (!NumberConverter.IsNumeric(numberOne) || !NumberConverter.IsNumeric(numberTwo)) {
            throw new UnsuportedMathOperationException("Please set a numeric value");
        }
        
        Double subtraction = math.subtraction(NumberConverter.CovertToDouble(numberOne), NumberConverter.CovertToDouble(numberTwo));
        return subtraction;
    }
    
    @RequestMapping(value="/division/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Double division(@PathVariable("numberOne") String numberOne, @PathVariable("numberTwo") String numberTwo) {
        
        if (!NumberConverter.IsNumeric(numberOne) || !NumberConverter.IsNumeric(numberTwo)) {
            throw new UnsuportedMathOperationException("Please set a numeric value");
        }
        
        Double division = math.division(NumberConverter.CovertToDouble(numberOne), NumberConverter.CovertToDouble(numberTwo));
        return division;
    }
    
    @RequestMapping(value="/multiplication/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Double multiplication(@PathVariable("numberOne") String numberOne, @PathVariable("numberTwo") String numberTwo) {
        
        if (!NumberConverter.IsNumeric(numberOne) || !NumberConverter.IsNumeric(numberTwo)) {
            throw new UnsuportedMathOperationException("Please set a numeric value");
        }
        
        Double multiplication = math.multiplication(NumberConverter.CovertToDouble(numberOne), NumberConverter.CovertToDouble(numberTwo));
        return multiplication;
    }
    
    @RequestMapping(value="/mean/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Double mean(@PathVariable("numberOne") String numberOne, @PathVariable("numberTwo") String numberTwo) {
        
        if (!NumberConverter.IsNumeric(numberOne) || !NumberConverter.IsNumeric(numberTwo)) {
            throw new UnsuportedMathOperationException("Please set a numeric value");
        }
        
        Double mean = math.mean(NumberConverter.CovertToDouble(numberOne), NumberConverter.CovertToDouble(numberTwo));
        return mean;
    }

    @RequestMapping(value="/squareRoot/{number}", method=RequestMethod.GET)
    public Double squareRoot(@PathVariable("number") String number) {
        
        if (!NumberConverter.IsNumeric(number)) {
            throw new UnsuportedMathOperationException("Please set a numeric value");
        }
        
        Double squareRoot = math.squareRoot(NumberConverter.CovertToDouble(number));
        return squareRoot;
    }

}