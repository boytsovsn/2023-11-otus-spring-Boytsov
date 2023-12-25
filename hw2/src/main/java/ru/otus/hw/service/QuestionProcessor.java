package ru.otus.hw.service;

import ru.otus.hw.domain.Question;

public interface QuestionProcessor {
    boolean checkAnswer(Question question);
}
