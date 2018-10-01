package br.com.erudio.math;

public class SimpleMath {

    public Long sum(Long firstNumber, Long secondNumber) {
        return firstNumber + secondNumber;
    }

    public Long subtraction(Long firstNumber, Long secondNumber) {
        return firstNumber - secondNumber;
    }

    public Long multiplication(Long firstNumber, Long secondNumber) {
        return firstNumber * secondNumber;
    }

    public Long division(Long firstNumber, Long secondNumber) {
        return firstNumber / secondNumber;
    }

    public Long mean(Long firstNumber, Long secondNumber) {
        return (firstNumber + secondNumber) / 2;
    }
    
    public Float squareRoot(Long number) {
        return (float) Math.sqrt(number);
    }
}
