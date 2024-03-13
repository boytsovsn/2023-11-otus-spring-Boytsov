package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.domain.entities.Author;
import ru.otus.hw.domain.entities.Book;

import java.util.List;

public interface BookService {
    Mono<Book> findById(String id);

    Flux<Book> findAll();

    Flux<Book> saveAll(List<Book> books);

    Mono<Book> insert(String title, String authorId, String genreId);

    Mono<Book> update(String id, String title, String authorId, String genreId);

    Mono<Void> deleteById(String id);
}
