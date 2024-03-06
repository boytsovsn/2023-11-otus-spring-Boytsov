package ru.otus.hw.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import ru.otus.hw.repository.*;

@EnableReactiveMongoRepositories(basePackageClasses = {PersonRepository.class, NotesRepository.class, AuthorRepository.class, GenreRepository.class, BookRepository.class, RemarkRepository.class})
//@EnableMongock
@Configuration
public class ReactiveMongoConfig {

}

