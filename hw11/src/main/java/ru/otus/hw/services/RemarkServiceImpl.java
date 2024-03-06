package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.domain.entities.Remark;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.RemarkRepository;

@Service
@RequiredArgsConstructor
public class RemarkServiceImpl implements RemarkService {

    private final RemarkRepository remarkRepository;

    private final BookRepository bookRepository;

    @Override
    public Flux<Remark> findByBookId(String id) {
        return bookRepository.findById(id).map(x->x.getRemarks())
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public Mono<Remark> findById(String id) {
        return remarkRepository.findById(id);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return remarkRepository.findById(id).flatMap((x)->{
            bookRepository.findById(x.getBook().getId()).subscribe(result->{
                result.getRemarks().remove(x);
                bookRepository.save(result);
            });
            return remarkRepository.deleteById(id);
        });
    }
}
