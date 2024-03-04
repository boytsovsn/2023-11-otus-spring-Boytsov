package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.spring.domain.entities.Genre;

import java.util.List;


public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {
    Flux<Genre> findAll();
}
