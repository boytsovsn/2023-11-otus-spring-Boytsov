package ru.otus.hw.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import ru.otus.hw.repository.*;

@EnableReactiveMongoRepositories(basePackages = {"ru.otus.hw.repository"})
@Configuration
public class ReactiveMongoConfig {

}

