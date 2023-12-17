package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            var isAnswerValid = false; // Задать вопрос, получить ответ
            ioService.printFormattedLine(question.text());
            ioService.printLine("Possible answers:");
            int i = 1;
            int trueN = 0;
            for (var answer : question.answers()) {
                ioService.printLine(i + ". " + answer.text());
                if (answer.isCorrect())
                    trueN = i;
                i++;
            }
            int n = ioService.readIntForRangeWithPrompt(1, i, "Get number of the correct answer: ", "Wrong number!");
            if (n==trueN)
                isAnswerValid = true;
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}
