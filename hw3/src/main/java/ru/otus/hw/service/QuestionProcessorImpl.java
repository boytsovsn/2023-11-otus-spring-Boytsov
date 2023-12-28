package ru.otus.hw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionProcessException;

@Service
public class QuestionProcessorImpl implements QuestionProcessor {

    private final LocalizedIOService localizedIOServiceImpl;


    public QuestionProcessorImpl(LocalizedIOService localizedIOServiceImpl) {
        this.localizedIOServiceImpl = localizedIOServiceImpl;
    }

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
            throw new QuestionProcessException(localizedIOServiceImpl.getMessage("QuestionProcessor.Question.is.null"));
        }
        if (question.answers() == null || question.answers().isEmpty()) {
            throw new QuestionProcessException(localizedIOServiceImpl.getMessage("QuestionProcessor.Question.has.no.answer"));
        }
         // Задать вопрос, получить ответ
        retreviewQuestionAnswers(question);
        int answerN = localizedIOServiceImpl.readIntForRangeWithPromptLocalized(1, question.answers().size(), "QuestionProcessor.Get.number.of.the.correct.answer", "QuestionProcessor.Wrong.number");
        return checkAnswer(question, answerN);
    }

    private void retreviewQuestionAnswers(Question question) {
        localizedIOServiceImpl.printFormattedLine(question.text());
        localizedIOServiceImpl.printLineLocalized("QuestionProcessor.Possible.answers");
        int i = 1;
        for (var answer : question.answers()) {
            localizedIOServiceImpl.printLine(i + ". " + answer.text());
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
        throw new QuestionProcessException(localizedIOServiceImpl.getMessage("QuestionProcessor.There.is.no.correct.answer.for.this.question", question.text()));
    }
}
