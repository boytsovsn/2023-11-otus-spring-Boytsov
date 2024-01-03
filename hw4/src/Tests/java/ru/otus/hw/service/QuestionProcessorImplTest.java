package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Интеграционный тест проверки ответов")
@SpringBootTest
class QuestionProcessorImplTest {

    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private QuestionProcessorImpl questionProcessorImpl;
    private List<Question> questions;
    private List<Integer> answersN;

    @BeforeEach
    void setUp() {
        questions = questionDao.findAll();
        answersN = new ArrayList<>();
    }

    @Test
    @DisplayName("Проверка правильных ответов")
    void checkCorrectAnswer() {
        int trueAnswerCount = 0;
        answersN.add(1);
        answersN.add(1);
        answersN.add(3);
        answersN.add(3);
        int i = 0;
        for (var question: questions) {
            if (questionProcessorImpl.checkAnswer(question, answersN.get(i++)))
                trueAnswerCount++;
            if (i>=answersN.size())
                break;
        }
        assertEquals(trueAnswerCount, 4);
    }

    @Test
    @DisplayName("Проверка полуправильных ответов")
    void checkSoSoAnswer() {
        int trueAnswerCount = 0;
        answersN.add(1);
        answersN.add(2);
        answersN.add(3);
        answersN.add(2);
        int i = 0;
        for (var question: questions) {
            if (questionProcessorImpl.checkAnswer(question, answersN.get(i++)))
                trueAnswerCount++;
            if (i>=answersN.size())
                break;
        }
        assertEquals(trueAnswerCount, 2);
    }

    @Test
    @DisplayName("Проверка неправильных ответов")
    void checkIncorrectAnswer() {
        int trueAnswerCount = 0;
        answersN.add(2);
        answersN.add(3);
        answersN.add(4);
        answersN.add(1);
        int i = 0;
        for (var question: questions) {
            if (questionProcessorImpl.checkAnswer(question, answersN.get(i++)))
                trueAnswerCount++;
            if (i>=answersN.size())
                break;
        }
        assertEquals(trueAnswerCount, 0);
    }

    @Test
    @DisplayName("Проверка полуправильных ответов, с ошибочным вводом номера ответа")
    void checkSoSoAnswerWithWrong() {
        int trueAnswerCount = 0;
        answersN.add(1);
        answersN.add(3);
        answersN.add(4);
        answersN.add(5);
        int i = 0;
        for (var question: questions) {
            if (questionProcessorImpl.checkAnswer(question, answersN.get(i++)))
                trueAnswerCount++;
            if (i>=answersN.size())
                break;
        }
        assertEquals(trueAnswerCount, 1);
    }
}