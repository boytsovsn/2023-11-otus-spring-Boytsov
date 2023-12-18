package ru.otus.hw.test.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.config.TestFileNameProvider;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan("ru.otus.hw.test")
public class AppPropTest implements TestConfig, TestFileNameProvider {
    // внедрить свойство из application.properties
    @Value("${test.rightAnswersCountToPass}")
    private int rightAnswersCountToPass;

    // внедрить свойство из application.properties
    @Value("${test.fileName}")
    private String testFileName;

    @Override
    public int getRightAnswersCountToPass() {
        return rightAnswersCountToPass;
    }

    @Override
    public String getTestFileName() {
        return testFileName;
    }
}
