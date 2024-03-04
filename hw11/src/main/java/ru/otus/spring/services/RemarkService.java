package ru.otus.spring.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.entities.Remark;

import java.util.List;
import java.util.Optional;

public interface RemarkService {

//    Flux<Remark> findByBookId(String id);

    Mono<Remark> findById(String id);

    Mono<Remark> save(String id, String remarkText, String bookId);

    void deleteById(String id);
}
