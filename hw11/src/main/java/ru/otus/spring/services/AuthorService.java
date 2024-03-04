package ru.otus.spring.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.entities.Author;

public interface AuthorService {
    Flux<Author> findAll();

    Mono<Author> findById(String id);
}
