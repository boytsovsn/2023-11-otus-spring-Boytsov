package ru.otus.hw.exceptions;

public class QuestionProcessException extends RuntimeException {
    public QuestionProcessException(String message, Throwable ex) {
        super(message, ex);
    }


    public QuestionProcessException(String message) {
        super(message);
    }
}
