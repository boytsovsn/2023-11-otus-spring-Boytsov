package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionProcessException;

@Service
@RequiredArgsConstructor
public class QuestionProcessorImpl implements QuestionProcessor {

    private final LocalizedIOService ioService;

    public boolean checkAnswer(Question question, int answerN) {
        boolean isAnswerValid = false;
        int trueN = getCorrectAnswers(question);
        if (answerN==trueN)
            isAnswerValid = true;
        return isAnswerValid;
    }

    @Override
    public boolean checkAnswer(Question question) {
        if (question == null) {
            throw new QuestionProcessException("Question is null!");
        }
        if (question.answers() == null || question.answers().isEmpty()) {
            throw new QuestionProcessException("Question has no answer!");
        }
         // Задать вопрос, получить ответ
        retreviewQuestionAnswers(question);
        int answerN = ioService.readIntForRangeWithPrompt(1, question.answers().size(), "Get number of the correct answer: ", "Wrong number!");
        return checkAnswer(question, answerN);
    }

    private void retreviewQuestionAnswers(Question question) {
        ioService.printFormattedLine(question.text());
        ioService.printLine("Possible answers:");
        int i = 1;
        for (var answer : question.answers()) {
            ioService.printLine(i + ". " + answer.text());
            i++;
        }
    }

    private int getCorrectAnswers(Question question) {
        int i = 1;
        int trueN = 0;
        for (var answer : question.answers()) {
            if (answer.isCorrect())
                return trueN = i;
            i++;
        }
        throw new QuestionProcessException("There is no correct answer for this question: " + question.text());
    }
}
