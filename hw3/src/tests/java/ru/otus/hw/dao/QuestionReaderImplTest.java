package ru.otus.hw.dao;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.support.ResourceBundleMessageSource;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.service.LocalizedMessagesService;
import ru.otus.hw.service.LocalizedMessagesServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DisplayName("Тест чтения вопросов и ответов из ресурсных файлов")
@SpringBootTest(classes = {QuestionReaderImpl.class})
class QuestionReaderImplTest {

    @MockBean
    private TestFileNameProvider testFileNameProvider;

    @MockBean
    private LocalizedMessagesService localizedMessagesService;

    @Autowired
    private QuestionReader questionReaderImpl;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    @DisplayName("Тест удачного чтения вопросов и ответов из ресурсных файлов")
    void readCsvQuestions() {
        Map<String, String> fileNameByLocaleTag =new HashMap<String, String>();
        fileNameByLocaleTag.put("ru-RU", "questions_ru.csv");
        Locale locale = Locale.forLanguageTag("ru-RU");
        AppProperties appProperties = new AppProperties(3, locale, fileNameByLocaleTag);
        testFileNameProvider = appProperties;
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        localizedMessagesService = new LocalizedMessagesServiceImpl(messageSource, appProperties);
        questionReaderImpl = new QuestionReaderImpl(testFileNameProvider, localizedMessagesService);
        List<QuestionDto> questDTO = questionReaderImpl.readCsvQuestions();
        assertEquals(questDTO.size(), 4);
    }

    @Test
    @DisplayName("Тест неудачного чтения вопросов и ответов из ресурсных файлов")
    void readWrongCsvQuestions() {
        Map<String, String> fileNameByLocaleTag =new HashMap<String, String>();
        fileNameByLocaleTag.put("ru-RU", "questions_r.csv");
        Locale locale = Locale.forLanguageTag("ru-RU");
        AppProperties appProperties = new AppProperties(3, locale, fileNameByLocaleTag);
        testFileNameProvider = appProperties;
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        localizedMessagesService = new LocalizedMessagesServiceImpl(messageSource, appProperties);
        questionReaderImpl = new QuestionReaderImpl(testFileNameProvider, localizedMessagesService);
        Throwable exception = Assertions.assertThrows(QuestionReadException.class, () -> {
            List<QuestionDto> questDTO = questionReaderImpl.readCsvQuestions();});
        assertEquals(exception.getMessage(), localizedMessagesService.getMessage("QuestionsReader.Questions.input.stream.is.null"));
    }
}