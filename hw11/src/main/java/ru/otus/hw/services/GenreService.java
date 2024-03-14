package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.domain.entities.Author;
import ru.otus.hw.domain.entities.Genre;

import java.util.List;

public interface GenreService {
    Flux<Genre> findAll();

    Flux<Genre> saveAll(List<Genre> genres);

    Mono<Genre> findById(String id);
}
