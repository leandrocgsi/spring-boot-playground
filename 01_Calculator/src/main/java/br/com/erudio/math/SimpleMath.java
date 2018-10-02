package br.com.erudio.math;

public class SimpleMath {

    public Float Sum(Float firstNumber, Float secondNumber) {
        return firstNumber + secondNumber;
    }

    public Float Subtraction(Float firstNumber, Float secondNumber) {
        return firstNumber - secondNumber;
    }

    public Float Multiplication(Float firstNumber, Float secondNumber) {
        return firstNumber * secondNumber;
    }

    public Float Division(Float firstNumber, Float secondNumber) {
        return firstNumber / secondNumber;
    }

    public Float Mean(Float firstNumber, Float secondNumber) {
        return (firstNumber + secondNumber) / 2;
    }
    
    public Float SquareRoot(Float number) {
        return (float) Math.sqrt(number);
    }
}