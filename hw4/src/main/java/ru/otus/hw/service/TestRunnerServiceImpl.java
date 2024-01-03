package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.ChangeLocal;
import ru.otus.hw.config.LocaleConfig;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@RequiredArgsConstructor
@ShellComponent
public class TestRunnerServiceImpl {

    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    private final ChangeLocal chLocal;

    private final LocalizedIOService localizedIOServiceImpl;

    private Student student;

    private TestResult testResult;

//    @Override
//    public void run() {
//
//    }

    @ShellMethod(value = "Change language command", key = {"la", "lang"})
    public String lang() {
        String res_lang = "";
        localizedIOServiceImpl.printLineLocalized("Shell.Choose.Language");
        localizedIOServiceImpl.printLineLocalized("Shell.English");
        localizedIOServiceImpl.printLineLocalized("Shell.Russian");
        int var = localizedIOServiceImpl.readIntForRangeWithPromptLocalized(1, 2, "Shell.Choose.Language", "QuestionProcessor.Wrong.number");
        if (var == 1) {
            chLocal.setLocale("en");
            res_lang = " en";
        }
        if (var == 2) {
            chLocal.setLocale("ru-RU");
            res_lang = " ru_RU";
        }
        String res = localizedIOServiceImpl.getMessage( "Shell.Application.Language", res_lang);
      return res;
    }

    @ShellMethod(value = "Login command", key = {"l", "login"})
    public String login(@ShellOption(defaultValue = "AnyUser") String userName) {
        student = studentService.determineCurrentStudent();
        return localizedIOServiceImpl.getMessage("Shell.You.have.registered");
    }

    @ShellMethod(value = "Test command", key = {"t", "test"})
    @ShellMethodAvailability(value = "isTestCommandAvailable")
    public String test() {
        testResult = testService.executeTestFor(student);
        return localizedIOServiceImpl.getMessage("Shell.Show.the.test.result");
    }

    @ShellMethod(value = "Show result command", key = {"s", "show"})
    @ShellMethodAvailability(value = "isShowCommandAvailable")
    public String show() {
        resultService.showResult(testResult);
        return localizedIOServiceImpl.getMessage("Shell.Exit");
    }

    private Availability isShowCommandAvailable() {
        return testResult == null? Availability.unavailable(localizedIOServiceImpl.getMessage("Shell.Pass.The.Test")): Availability.available();
    }

    private Availability isTestCommandAvailable() {
        return student == null? Availability.unavailable(localizedIOServiceImpl.getMessage("Shell.First.register")): Availability.available();
    }

//    @Override
//    public void run(String... args) throws Exception {
//        if ((testResult != null)&&(student!=null))
//            show();
//    }

}
