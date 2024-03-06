package ru.otus.hw.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.domain.entities.Genre;


public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {
    Flux<Genre> findAll();
}
