package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.hw.repository", "ru.otus.hw.services", "ru.otus.hw.config.test.reactiverest"})
class BookServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImplTest.class);

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RemarkRepository remarkRepository;

    @Autowired
    private Scheduler workerPool;

    private Remark remark;

    private Book testBook;

    private Author author;

    private Genre genre;

    @BeforeEach
    void setUp() {
        authorRepository.deleteAll().block();
        genreRepository.deleteAll().block();
        bookRepository.deleteAll().block();
        remarkRepository.deleteAll().block();
        Author author1 = new Author( "Pushkin");
        author = authorRepository.save(author1).block();
        Genre genre1 = new Genre( "Poema");
        genre =genreRepository.save(genre1).block();
        Book book1 = new Book( "Evgeniy Onegin", author, genre);
        Book book2 = bookRepository.save(book1).block();
        remark = remarkRepository.save(new Remark("Cool! Easy read!", book2.getId())).block();
        book2.setRemarks(Arrays.asList(remark));
        testBook = bookRepository.save(book2).block();
    }

    @Test
    @DisplayName("Поиск книги по id и выдача её с вместе с отзывами")
    void testFindById() {
        String id = testBook.getId();
        Mono<Book> monoBook =  bookRepository.findById(id).switchIfEmpty(Mono.error(new EntityNotFoundException("Book not found " + id)));
        AtomicReference<String> author1 = new AtomicReference<>("");
        AtomicReference<String> genre1 = new AtomicReference<>("");
        AtomicReference<List<Remark>> remarks1 = new AtomicReference<>(new ArrayList<Remark>());
        StepVerifier
                .create(monoBook)
                .assertNext(book -> {
                    author1.set(book.getAuthorId());
                    genre1.set(book.getGenreId());
                    remarks1.set(book.getRemarks());})
                .expectComplete()
                .verify();
        assertEquals(author.getId(), author1.get());
        assertEquals(genre.getId(), genre1.get());
        assertEquals(testBook.getRemarks(), remarks1.get());
    }

    @Test
    @DisplayName("Удвление книги по id вместе с отзывами")
    void testDeleteById() {
        String id = testBook.getId();
        Mono<Void> monoDel = bookRepository.findById(id).switchIfEmpty(Mono.error(new EntityNotFoundException("Book not found " + id)))
                .flatMap(book->{
                    remarkRepository.deleteAllById(book.getRemarks().stream().map(Remark::getId).toList()).publishOn(workerPool).subscribe();
                    return bookRepository.deleteById(id);
                });
        StepVerifier
                .create(monoDel)
                .verifyComplete();
        Book actualBook = bookRepository.findById(testBook.getId()).block();
        assertEquals(actualBook, null);
        Remark actualRemark = remarkRepository.findById(remark.getId()).block();
        assertEquals(actualRemark, null);
    }

    @Test
    @DisplayName("Сохранение книги по id")
    void testUpdateById() {
        Author author1 = new Author( "Tolstoy");
        Author authorUpdate = authorRepository.save(author1).block();
        Genre genre1 = new Genre( "Roman");
        Genre genreUpdate =genreRepository.save(genre1).block();
        String id = testBook.getId();
        Mono<Book> monoBook = bookRepository.findById(id).switchIfEmpty(Mono.error(new EntityNotFoundException("Book not found " + id)))
                .flatMap(x-> {
                    x.setAuthorId(authorUpdate.getId());
                    x.setGenreId(genreUpdate.getId());
                    x.setTitle("Voina i mir");
                    return bookRepository.save(x);
                });
        AtomicReference<Book> bookUpdated = new AtomicReference<>(new Book());
        StepVerifier
                .create(monoBook)
                .assertNext(book->{bookUpdated.set(book);})
                .verifyComplete();
        Book actualBook = bookRepository.findById(testBook.getId()).block();
        assertEquals(actualBook, bookUpdated.get());
        Remark actualRemark = remarkRepository.findById(remark.getId()).block();
        assertEquals(actualRemark, bookUpdated.get().getRemarks().get(0));
    }
    @Test
    @DisplayName("Вставка новой книги")
    void testInsert() {
        Author author1 = new Author( "Tolstoy");
        Author authorInsert = authorRepository.save(author1).block();
        Genre genre1 = new Genre( "Roman");
        Genre genreInsert =genreRepository.save(genre1).block();
        String id = testBook.getId();
        Mono<Book> monoBook = Mono.just(new Book())
                .flatMap(x -> {
                    x.setAuthorId(authorInsert.getId());
                    x.setGenreId(genreInsert.getId());
                    x.setTitle("Voina i mir");
                    return bookRepository.save(x);
                });
        AtomicReference<Book> bookInserted = new AtomicReference<>(new Book());
        StepVerifier
                .create(monoBook)
                .assertNext(book->{bookInserted.set(book);})
                .verifyComplete();
        Book actualBook = bookRepository.findById(bookInserted.get().getId()).block();
        assertEquals(actualBook, bookInserted.get());
    }
}