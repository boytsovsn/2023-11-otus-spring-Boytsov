package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.domain.entities.Author;

public interface AuthorService {
    Flux<Author> findAll();

    Mono<Author> findById(String id);
}
