package br.com.erudio.math;

public class SimpleMath {

    public Float sum(Float firstNumber, Float secondNumber) {
        return firstNumber + secondNumber;
    }

    public Float subtraction(Float firstNumber, Float secondNumber) {
        return firstNumber - secondNumber;
    }

    public Float multiplication(Float firstNumber, Float secondNumber) {
        return firstNumber * secondNumber;
    }

    public Float division(Float firstNumber, Float secondNumber) {
        return firstNumber / secondNumber;
    }

    public Float mean(Float firstNumber, Float secondNumber) {
        return (firstNumber + secondNumber) / 2;
    }
    
    public Float squareRoot(Float number) {
        return (float) Math.sqrt(number);
    }
}