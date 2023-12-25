package ru.otus.hw.test.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.dao.QuestionReader;
import ru.otus.hw.dao.QuestionReaderImpl;
import ru.otus.hw.domain.Question;
import ru.otus.hw.service.IOService;
import ru.otus.hw.service.QuestionProcessorImpl;
import ru.otus.hw.service.StreamsIOService;
import ru.otus.hw.test.config.AppPropTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(classes = QuestionProcessorImplTest.class)
class QuestionProcessorImplTest {

    private QuestionDao questionDao;
    private IOService ioService;
    private QuestionProcessorImpl questionProcessor;

    private QuestionReader questionReader;

    private List<Question> questions;

    private List<Integer> answersN;

    private TestFileNameProvider testFileNameProvider;

    @BeforeEach
    void setUp() {
        ioService = new StreamsIOService(System.out, System.in);
        questionProcessor = new QuestionProcessorImpl(ioService);
        testFileNameProvider = new AppPropTest(3, "questions.csv");
        questionReader = new QuestionReaderImpl(testFileNameProvider);
        questionDao = new CsvQuestionDao(questionReader);
        questions = questionDao.findAll();
        answersN = new ArrayList<>();
    }

    @Test
    void checkCorrectAnswer() {
        int trueAnswerCount = 0;
        answersN.add(1);
        answersN.add(1);
        answersN.add(3);
        answersN.add(3);
        int i = 0;
        for (var question: questions) {
            if (questionProcessor.checkAnswer(question, answersN.get(i++)))
                trueAnswerCount++;
            if (i>=answersN.size())
                break;
        }
        assertEquals(trueAnswerCount, 4);
    }

    @Test
    void checkSoSoAnswer() {
        int trueAnswerCount = 0;
        answersN.add(1);
        answersN.add(2);
        answersN.add(3);
        answersN.add(2);
        int i = 0;
        for (var question: questions) {
            if (questionProcessor.checkAnswer(question, answersN.get(i++)))
                trueAnswerCount++;
            if (i>=answersN.size())
                break;
        }
        assertEquals(trueAnswerCount, 2);
    }

    @Test
    void checkIncorrectAnswer() {
        int trueAnswerCount = 0;
        answersN.add(2);
        answersN.add(3);
        answersN.add(4);
        answersN.add(1);
        int i = 0;
        for (var question: questions) {
            if (questionProcessor.checkAnswer(question, answersN.get(i++)))
                trueAnswerCount++;
            if (i>=answersN.size())
                break;
        }
        assertEquals(trueAnswerCount, 0);
    }

    @Test
    void checkSoSoAnswerWithWrong() {
        int trueAnswerCount = 0;
        answersN.add(1);
        answersN.add(3);
        answersN.add(4);
        answersN.add(5);
        int i = 0;
        for (var question: questions) {
            if (questionProcessor.checkAnswer(question, answersN.get(i++)))
                trueAnswerCount++;
            if (i>=answersN.size())
                break;
        }
        assertEquals(trueAnswerCount, 1);
    }
}