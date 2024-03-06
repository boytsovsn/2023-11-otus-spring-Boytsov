package ru.otus.hw.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.domain.entities.Author;


public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {

    Flux<Author> findAll();
}
