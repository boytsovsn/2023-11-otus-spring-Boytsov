package ru.otus.hw.test.config;

import ru.otus.hw.config.TestConfig;
import ru.otus.hw.config.TestFileNameProvider;

//@Configuration
////@PropertySource("classpath:/test-classes/application.properties")
//@Setter
//@RequiredArgsConstructor
public class AppPropTest implements TestConfig, TestFileNameProvider {
    // внедрить свойство из application.properties
//    @Value("${test.rightAnswersCountToPass}")
    private int rightAnswersCountToPass;

    // внедрить свойство из application.properties
//    @Value("${test.questions}")
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
