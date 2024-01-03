package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.CommandNotCurrentlyAvailable;
import org.springframework.shell.InputProvider;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.config.ApplicationConfig;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionReaderImpl;
import ru.otus.hw.service.*;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Тест проверки команд командной строки")
@SpringBootTest
public class TestRunnerServiceImplTest {

    private InputProvider inputProvider;

    @Autowired
    private Shell testRunnerServiceImpl;

    @BeforeEach
    void setUp() {
        inputProvider = mock(InputProvider.class);
    }

    @DisplayName(" должен возвращать CommandNotCurrentlyAvailable, если при попытке выполнения команды test пользователь не выполнил вход")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnCommandNotCurrentlyAvailableObjectWhenUserDoesNotLogin() {
        when(inputProvider.readInput())
                .thenReturn(() -> "test")
                .thenReturn(null);

        assertThatCode(() -> testRunnerServiceImpl.run(inputProvider)).isInstanceOf(CommandNotCurrentlyAvailable.class);
    }

    @DisplayName(" должен возвращать CommandNotCurrentlyAvailable, если при попытке выполнения команды show пользователь не выполнил test")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnCommandNotCurrentlyAvailableObjectWhenUserDoesNotTestBeforeShow() {
        when(inputProvider.readInput())
                .thenReturn(() -> "show")
                .thenReturn(null);

        assertThatCode(() -> testRunnerServiceImpl.run(inputProvider)).isInstanceOf(CommandNotCurrentlyAvailable.class);
    }
}
