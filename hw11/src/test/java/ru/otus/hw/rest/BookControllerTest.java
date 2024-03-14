package ru.otus.hw.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw.domain.dto.AuthorDto;
import ru.otus.hw.domain.dto.BookDto;
import ru.otus.hw.domain.dto.GenreDto;
import ru.otus.hw.domain.entities.Author;
import ru.otus.hw.domain.entities.Book;
import ru.otus.hw.domain.entities.Genre;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.GenreRepository;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.google.common.net.HttpHeaders.ACCEPT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@EnableConfigurationProperties
@ComponentScan({"ru.otus.hw.config.test", "ru.otus.hw.config.test.reactiverest", "ru.otus.hw.services", "ru.otus.hw.repository"})
@SpringBootTest(classes = {BookController.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class BookControllerTest {

    @Autowired
    BookService bookService;

    @Autowired
    AuthorService authorService;

    @Autowired
    GenreService genreService;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    private WebTestClient webTestClient;

    @LocalServerPort
    private int port;

    private List<Author> expectedAuthors;

    private List<Genre> expectedGenres;

    private List<Book> expectedBooks;

    private static String[] aStrings = {"Pushkin", "Lermontov", "Tolstoi"};
    private static String[] gStrings = {"Skazka", "Poema", "Roman"};
    private static String[] bStrings = {"Zolotaya rybka", "Borodino", "Anna Karenina"};

    @BeforeEach
    void setUp() {
        expectedAuthors = new ArrayList<>();
        expectedGenres = new ArrayList<>();
        expectedBooks = new ArrayList<>();
        authorRepository.deleteAll().block();
        genreRepository.deleteAll().block();
        bookRepository.deleteAll().block();
        for (int i=0; i<bStrings.length; i++) {
            Author author = new Author(aStrings[i]);
            expectedAuthors.add(authorRepository.save(author).block());
            Genre genre = new Genre(gStrings[i]);
            expectedGenres.add(genreRepository.save(genre).block());
            Book book = new Book(bStrings[i], author, genre);
            expectedBooks.add(bookRepository.save(book).block());
        }
    }

    @Test
    @DisplayName("Список книг")
    void bookList() {
        var webTestClientForTest = webTestClient.mutate()
                .responseTimeout(Duration.ofSeconds(1))
                .build();
        var result = webTestClientForTest
                .get().uri("/api/book")
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody();
        var step = StepVerifier.create(result);
        StepVerifier.Step<BookDto> stepResult = null;
        for (var idx = 1; idx <= expectedBooks.size(); idx++) {
            BookDto bookDto = BookDto.fromDomainObject(expectedBooks.get(idx-1));
            if (idx == 1) {
                bookDto.setAuthors(expectedAuthors.stream().map(AuthorDto::fromDomainObject).toList());
                bookDto.setGenres(expectedGenres.stream().map(GenreDto::fromDomainObject).toList());
            }
            stepResult = step.expectNext(bookDto);
        }
        stepResult.verifyComplete();
    }

    @Test
    @DisplayName("Книга")
    void getBook() {
        var webTestClientForTest = webTestClient.mutate()
                .responseTimeout(Duration.ofSeconds(1))
                .build();
        for (var idx = 1; idx <= expectedBooks.size(); idx++) {
            var result = webTestClientForTest
                .get().uri("/api/book/"+expectedBooks.get(idx-1).getId())
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody();
            var step = StepVerifier.create(result);
            StepVerifier.Step<BookDto> stepResult = null;
            BookDto bookDto = BookDto.fromDomainObject(expectedBooks.get(idx-1));
            stepResult = step.expectNext(bookDto);
            stepResult.verifyComplete();
        }
    }

    @Test
    void deleteBook() {
        var webTestClientForTest = webTestClient.mutate()
                .responseTimeout(Duration.ofSeconds(1))
                .build();
        var result = webTestClientForTest
                    .delete().uri("/api/book/"+expectedBooks.get(0).getId())
                    .exchange()
                    .expectStatus().isOk();
        Book findBook = bookRepository.findById(expectedBooks.get(0).getId()).block();
        assertEquals(findBook, null);
    }

    @Test
    void editBook() {
        var webTestClientForTest = webTestClient.mutate()
                .responseTimeout(Duration.ofSeconds(1))
                .build();
        BookDto updatedBook = BookDto.fromDomainObject(expectedBooks.get(0));
        updatedBook.setTitle("Другая книга");
        updatedBook.setAuthorId(expectedBooks.get(1).getAuthorId());
        updatedBook.setGenreId(expectedBooks.get(2).getGenreId());
        var result = webTestClientForTest
                .put().uri("/api/book/"+expectedBooks.get(0).getId())
                .body(Mono.just(updatedBook), BookDto.class)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk();
        BookDto actualBook = bookRepository.findById(expectedBooks.get(0).getId()).map(BookDto::fromDomainObject).block();
        assertEquals(updatedBook, actualBook);
    }

    @Test
    void createBook() {
        var webTestClientForTest = webTestClient.mutate()
                .responseTimeout(Duration.ofSeconds(1))
                .build();
        BookDto newBookDto = BookDto.fromDomainObject(new Book());
        newBookDto.setTitle("Новая книга");
        newBookDto.setAuthorId(expectedBooks.get(2).getAuthorId());
        newBookDto.setGenreId(expectedBooks.get(1).getGenreId());
        var result = webTestClientForTest
                .post().uri("/api/book")
                .body(Mono.just(newBookDto), BookDto.class)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isCreated()
                .returnResult(BookDto.class)
                .getResponseBody();
        var step = StepVerifier.create(result);
        StepVerifier.Step<BookDto> stepResult = null;
        AtomicReference<String> id = new AtomicReference<>("");
        stepResult = step.assertNext(bookDto -> {
            id.set(bookDto.getId()); assertNotNull(bookDto.getId());});
        stepResult.verifyComplete();
        BookDto actualBook = bookRepository.findById(id.get()).map(BookDto::fromDomainObject).block();
        newBookDto.setId(id.get());
        assertEquals(newBookDto, actualBook);
    }
}