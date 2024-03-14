package ru.otus.hw.config.test;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories(basePackages = {"ru.otus.hw.repository"})
@Configuration
public class ReactiveMongoConfig {

}
