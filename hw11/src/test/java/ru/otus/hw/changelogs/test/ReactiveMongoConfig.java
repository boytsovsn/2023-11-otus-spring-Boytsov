package ru.otus.hw.changelogs.test;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import ru.otus.hw.repository.PersonRepository;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@EnableReactiveMongoRepositories(basePackages = {"ru.otus.hw.repository"})
//@EnableMongock
@Configuration
public class ReactiveMongoConfig {

}
