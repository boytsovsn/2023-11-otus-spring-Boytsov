package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.spring.domain.entities.Author;


public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {

    Flux<Author> findAll();
}
