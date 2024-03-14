package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.domain.entities.Remark;

public interface RemarkService {

    Flux<Remark> findByBookId(String id);

    Mono<Remark> findById(String id);

    Mono<Void> deleteById(String id);
}
