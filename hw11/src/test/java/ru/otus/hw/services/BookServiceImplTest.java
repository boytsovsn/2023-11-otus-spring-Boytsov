package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
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

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.hw.changelogs.test", "ru.otus.hw.repository"})
class BookServiceImplTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RemarkRepository remarkRepository;

    private Remark remark;

    private Book book;

    private Author author;

    private Genre genre;

    @BeforeEach
    void setUp() {
        authorRepository.deleteAll();
        genreRepository.deleteAll();
        bookRepository.deleteAll();
        Author author1 = new Author( "Pushkin");
        author = authorRepository.save(author1).block();
        Genre genre1 = new Genre( "Poema");
        genre =genreRepository.save(genre1).block();
        Book book1 = new Book( "Evgeniy Onegin", author, genre);
        Book book2 = bookRepository.save(book1).block();
        remark = remarkRepository.save(new Remark("Cool! Easy read!", book2.getId())).block();
        book2.setRemarks(Arrays.asList(remark));
        book = bookRepository.save(book2).block();
    }

    @Test
    void findById() {
        String id = book.getId();
        Mono<Book> monoBook = bookRepository.findById(id).switchIfEmpty(Mono.error(new EntityNotFoundException("Book not found " + id)))
                .flatMap(book->
                        bookRepository.findById(id).switchIfEmpty(Mono.error(new EntityNotFoundException("Book not found " + id)))
                            .thenMany(remarkRepository.findAll()).filter(remark -> remark.getBookId().equals(id)).collectList()
                            .flatMap(remarks->{
                                book.setRemarks(remarks);
                                return Mono.just(book);
                            })
                );
        AtomicReference<String> author1 = new AtomicReference<>("");
        AtomicReference<String> genre1 = new AtomicReference<>("");
        StepVerifier
                .create(monoBook)
                .assertNext(book -> {
                    author1.set(book.getAuthorId());
                    genre1.set(book.getGenreId());})
                .expectComplete()
                .verify();
        assertEquals(author.getId(), author1.get());
        assertEquals(genre.getId(), genre1.get());
    }
}