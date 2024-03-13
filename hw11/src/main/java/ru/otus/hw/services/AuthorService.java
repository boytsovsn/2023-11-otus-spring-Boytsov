package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.domain.entities.Author;

import java.util.List;

public interface AuthorService {
    Flux<Author> findAll();

    Flux<Author> saveAll(List<Author> authors);

    Mono<Author> findById(String id);
}
