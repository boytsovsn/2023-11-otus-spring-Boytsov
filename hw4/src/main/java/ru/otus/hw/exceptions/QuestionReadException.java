package ru.otus.hw.exceptions;

public class QuestionReadException extends QuestionProcessException {
    public QuestionReadException(String message, Throwable ex) {
        super(message, ex);
    }
    public QuestionReadException(String message) {
        super(message);
    }
}
