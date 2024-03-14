package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import ru.otus.hw.domain.entities.Author;
import ru.otus.hw.domain.entities.Book;
import ru.otus.hw.domain.entities.Genre;
import ru.otus.hw.domain.entities.Remark;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.GenreRepository;
import ru.otus.hw.repository.RemarkRepository;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.hw.repository", "ru.otus.hw.config.test.reactiverest"})
class RemarkServiceImplTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private RemarkRepository remarkRepository;

    @Autowired
    private BookRepository bookRepository;

    private Remark remark;

    private Book testBook;

    @Autowired
    private Scheduler workerPool;

    @BeforeEach
    void setUp() {
        authorRepository.deleteAll().block();
        genreRepository.deleteAll().block();
        bookRepository.deleteAll().block();
        remarkRepository.deleteAll().block();
        Author author1 = new Author( "Pushkin");
        Author author = authorRepository.save(author1).block();
        Genre genre1 = new Genre( "Poema");
        Genre genre =genreRepository.save(genre1).block();
        Book book1 = new Book( "Evgeniy Onegin", author, genre);
        Book book2 = bookRepository.save(book1).block();
        remark = remarkRepository.save(new Remark("Cool! Easy read!", book2.getId())).block();
        book2.setRemarks(Arrays.asList(remark));
        testBook = bookRepository.save(book2).block();
    }

    @Test
    @DisplayName("Удаление замечания из таблицы замечаний и из книги")
    void testDeleteById() {
        Mono<Void> delRemark = remarkRepository.findById(remark.getId())
                .flatMap(res -> bookRepository.findById(res.getBookId())
                        .flatMap(res1 -> {
                            res1.getRemarks().remove(res);
                            bookRepository.save(res1).publishOn(workerPool).subscribe();
                            return remarkRepository.deleteById(res.getId());
                        }));

        StepVerifier
                .create(delRemark)
                .verifyComplete();

        Book actualBook = bookRepository.findById(testBook.getId()).block();
        assertEquals(actualBook.getRemarks().size(), 0);
        Remark actualRemark = remarkRepository.findById(remark.getId()).block();
        assertEquals(actualRemark, null);
    }

    @Test
    @DisplayName("Поиск замечания по книге")
    void testFindByBookId() {
        String id = testBook.getId();
        Flux<Remark> bookRemarks = bookRepository.findById(id).switchIfEmpty(Mono.error(new EntityNotFoundException("Book not found " + id)))
                .thenMany(remarkRepository.findAll()).filter(remark -> remark.getBookId().equals(id));
        AtomicReference<String>  actualId = new AtomicReference<String>("");
        StepVerifier
                .create(bookRemarks)
                .assertNext(remark -> {
                    actualId.set(remark.getId()); assertNotNull(remark.getId());})
                .expectComplete()
                .verify();

        Book actualBook = bookRepository.findById(testBook.getId()).block();
        assertEquals(actualBook.getRemarks().size(), 1);
        Remark expectedRemark = remarkRepository.findById(remark.getId()).block();
        assertEquals(expectedRemark.getId(), actualId.get());
    }
}