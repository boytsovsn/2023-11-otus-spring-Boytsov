package ru.otus.spring.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.entities.Genre;

public interface GenreService {
    Flux<Genre> findAll();

    Mono<Genre> findById(String id);
}
