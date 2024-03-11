package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw.domain.entities.Author;
import ru.otus.hw.domain.entities.Book;
import ru.otus.hw.domain.entities.Genre;
import ru.otus.hw.domain.entities.Remark;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.GenreRepository;
import ru.otus.hw.repository.RemarkRepository;

import java.lang.reflect.Array;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.hw.changelogs.test", "ru.otus.hw.repository"})
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

    @BeforeEach
    void setUp() {
        authorRepository.deleteAll();
        genreRepository.deleteAll();
        bookRepository.deleteAll();
        Author author1 = new Author( "Pushkin");
        Author author = authorRepository.save(author1).block();
        Genre genre1 = new Genre( "Poema");
        Genre genre =genreRepository.save(genre1).block();
        Book book1 = new Book( "Evgeniy Onegin", author, genre);
        Book book2 = bookRepository.save(book1).block();
        remark = remarkRepository.save(new Remark("Cool! Easy read!", book2.getId())).block();
        book2.setRemarks(Arrays.asList(remark));
        Book book3 = bookRepository.save(book2).block();
    }

    @Test
    void shouldBeDeleted() {
        Mono<Object> delRemark = remarkRepository.findById(remark.getId())
                .map(res -> {
                    Mono<Book> book = bookRepository.findById(res.getBookId())
                            .flatMap(res1 -> {
                                res1.getRemarks().remove(res);
                                return bookRepository.save(res1);
                            });
                    return res;
                })
                .flatMap(res2 -> remarkRepository.deleteById(res2.getId()));

        StepVerifier
                .create(delRemark)
                .verifyComplete();

    }
}