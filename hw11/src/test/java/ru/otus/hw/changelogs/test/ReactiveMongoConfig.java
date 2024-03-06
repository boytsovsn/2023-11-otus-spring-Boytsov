package ru.otus.hw.changelogs.test;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import ru.otus.hw.repository.PersonRepository;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@EnableReactiveMongoRepositories(basePackageClasses = {PersonRepository.class})
@Configuration
public class ReactiveMongoConfig {

}
