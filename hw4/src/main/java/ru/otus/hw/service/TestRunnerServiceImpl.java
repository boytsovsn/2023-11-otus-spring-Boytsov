package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
@ShellComponent
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    private Student student;

    private TestResult testResult;

    @Override
    public void run() {

    }

    @ShellMethod(value = "Login command", key = {"l", "login"})
    public String login(@ShellOption(defaultValue = "AnyUser") String userName) {
        student = studentService.determineCurrentStudent();
        return "Вы зарегистрировались. Пройти тест - команда (test).";
    }

    @ShellMethod(value = "Test command", key = {"t", "test"})
    @ShellMethodAvailability(value = "isTestCommandAvailable")
    public String test() {
        testResult = testService.executeTestFor(student);
        return "Посмотреть результат теста (команда show).";
    }

    @ShellMethod(value = "Show result command", key = {"s", "show"})
    @ShellMethodAvailability(value = "isShowCommandAvailable")
    public String show() {
        resultService.showResult(testResult);
        return "Выйти - команда exit.";
    }

    private Availability isShowCommandAvailable() {
        return testResult == null? Availability.unavailable("Пройдите тест (команда тест)."): Availability.available();
    }

    private Availability isTestCommandAvailable() {
        return student == null? Availability.unavailable("Сначала зарегистрируйтесь (команда login)."): Availability.available();
    }

    @Override
    public void run(String... args) throws Exception {
    }

}
