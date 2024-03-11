package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.hw.domain.entities.Book;
import ru.otus.hw.domain.entities.Remark;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.GenreRepository;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final RemarkService remarkServiceImpl;

    private final Scheduler workerPool;

    @Override
    public Mono<Book> findById(String id) {
        return bookRepository.findById(id).switchIfEmpty(Mono.error(new EntityNotFoundException("Book not found " + id)))
            .flatMap(book->
                remarkServiceImpl.findByBookId(id).collectList().flatMap(remarks->{
                    book.setRemarks(remarks);
                    return Mono.just(book);
                }));
    }

    @Override
    public Flux<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Mono<Book> insert(String title, String authorId, String genreId) {
        return save(null, title, authorId, genreId);
    }


    @Override
    public Mono<Book> update(String id, String title, String authorId, String genreId) {
        return save(id, title, authorId, genreId);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return bookRepository.findById(id).switchIfEmpty(Mono.error(new EntityNotFoundException("Book not found " + id)))
           .flatMap(book->{
               remarkServiceImpl.findByBookId(id).flatMap(x->deleteRemark(x));
               return bookRepository.deleteById(id);
           });
    }

    public Mono<Void> deleteRemark(Remark remark) {
        return remarkServiceImpl.deleteById(remark.getId());

    }

    private Mono<Book> save(String id, String title, String authorId, String genreId) {
        if (id!=null && !id.isEmpty() && !id.equalsIgnoreCase("0")) {
            return bookRepository.findById(id).switchIfEmpty(Mono.error(new EntityNotFoundException("Book not found " + id)))
                .flatMap((x)->{
                    x.setAuthorId(authorId);
                    x.setGenreId(genreId);
                    x.setTitle(title);
                    remarkServiceImpl.findByBookId(id).collectList().flatMap(remarks->{
                        x.setRemarks(remarks);
                        return Mono.just(x);
                    });
                    return bookRepository.save(x);
                });
        } else {
           return Mono.just(new Book())
               .flatMap((x)->{
                   x.setAuthorId(authorId);
                   x.setGenreId(genreId);
                   x.setTitle(title);
                   return bookRepository.save(x);
               });
        }
    }
}
