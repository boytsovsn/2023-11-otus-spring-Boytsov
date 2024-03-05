package ru.otus.spring.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.entities.Remark;

public interface RemarkService {

    Flux<Remark> findByBookId(String id);

    Mono<Remark> findById(String id);

    Mono<Remark> save(String id, String remarkText, String bookId);

    Mono<Void> deleteById(String id);
}
