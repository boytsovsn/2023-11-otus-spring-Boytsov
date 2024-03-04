package ru.otus.spring.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.entities.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Mono<Book> findById(String id);

    Flux<Book> findAll();

    Mono<Book> insert(String title, String authorId, String genreId);

    Mono<Book> update(String id, String title, String authorId, String genreId);

    void deleteById(String id);
}
