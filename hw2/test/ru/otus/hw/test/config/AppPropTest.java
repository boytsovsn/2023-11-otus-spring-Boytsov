package ru.otus.hw.test.config;

import ru.otus.hw.config.TestConfig;
import ru.otus.hw.config.TestFileNameProvider;

public class AppPropTest implements TestConfig, TestFileNameProvider {

    private int rightAnswersCountToPass;

    private String testFileName;

    public AppPropTest(int rightAnswersCountToPass_, String testFileName_) {
        rightAnswersCountToPass = rightAnswersCountToPass_;
        testFileName = testFileName_;
    }

    @Override
    public int getRightAnswersCountToPass() {
        return rightAnswersCountToPass;
    }

    @Override
    public String getTestFileName() {
        return testFileName;
    }
}
