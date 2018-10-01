package br.com.erudio.math;

public class SimpleMath {

    public Long Sum(Long firstNumber, Long secondNumber) {
        return firstNumber + secondNumber;
    }

    public Long Subtraction(Long firstNumber, Long secondNumber) {
        return firstNumber - secondNumber;
    }

    public Long Multiplication(Long firstNumber, Long secondNumber) {
        return firstNumber * secondNumber;
    }

    public Long Division(Long firstNumber, Long secondNumber) {
        return firstNumber / secondNumber;
    }

    public Long Mean(Long firstNumber, Long secondNumber) {
        return (firstNumber + secondNumber) / 2;
    }
    
    public Float SquareRoot(Long number) {
        return (float) Math.sqrt(number);
    }
}