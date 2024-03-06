package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.domain.entities.Genre;

public interface GenreService {
    Flux<Genre> findAll();

    Mono<Genre> findById(String id);
}