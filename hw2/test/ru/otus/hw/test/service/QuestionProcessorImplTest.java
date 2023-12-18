package ru.otus.hw.test.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.otus.hw.service.IOService;
import ru.otus.hw.service.QuestionProcessor;
import ru.otus.hw.service.QuestionProcessorImpl;
import ru.otus.hw.service.StreamsIOService;
import ru.otus.hw.test.config.AppPropTest;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.dao.QuestionReader;
import ru.otus.hw.dao.QuestionReaderImpl;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = AppPropTest.class)
class QuestionProcessorImplTest {

    private QuestionDao questionDao;
    private IOService ioService;
    private QuestionProcessor questionProcessor;

    private QuestionReader questionReader;

    private TestFileNameProvider testFileNameProvider;

    private List<Question> questions;

    private List<Integer> answersN;

    @BeforeEach
    void setUp() {
        ioService = new StreamsIOService(System.out, System.in);
        questionProcessor = new QuestionProcessorImpl(ioService);
        testFileNameProvider = new AppPropTest();
        questionReader = new QuestionReaderImpl(testFileNameProvider);
        questionDao = new CsvQuestionDao(questionReader);
        questions = questionDao.findAll();
        answersN = new ArrayList<>();
    }

    @Test
    void checkCorrectAnswer() {
        int trueAnswerCount = 0;
        answersN.clear();
        answersN.add(1);
        answersN.add(1);
        answersN.add(3);
        for (var question: questions) {
            if (questionProcessor.checkAnswer(question))
                trueAnswerCount++;
        }
        assertEquals(trueAnswerCount, 3);
    }

    @Test
    void checkSoSoAnswer() {
        int trueAnswerCount = 0;
        answersN.clear();
        answersN.add(1);
        answersN.add(2);
        answersN.add(3);
        for (var question: questions) {
            if (questionProcessor.checkAnswer(question))
                trueAnswerCount++;
        }
        assertEquals(trueAnswerCount, 2);
    }

    @Test
    void checkIncorrectAnswer() {
        int trueAnswerCount = 0;
        answersN.clear();
        answersN.add(2);
        answersN.add(3);
        answersN.add(4);
        for (var question: questions) {
            if (questionProcessor.checkAnswer(question))
                trueAnswerCount++;
        }
        assertEquals(trueAnswerCount, 0);
    }
}