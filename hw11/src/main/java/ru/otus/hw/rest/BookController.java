package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.otus.hw.domain.dto.AuthorDto;
import ru.otus.hw.domain.dto.BookDto;
import ru.otus.hw.domain.dto.GenreDto;
import ru.otus.hw.domain.entities.Book;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.NoSuchElementException;

import static reactor.core.publisher.Mono.just;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private static MultiValueMap<String, String> header = new LinkedMultiValueMap<>();

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/api/book")
    public Flux<BookDto> bookList() {
        final boolean[] first = {true};
        return bookService.findAll().map(BookDto::fromDomainObject).publishOn(Schedulers.boundedElastic()).flatMap((x)->{
            if (first[0]) {
                x.setAuthors(authorService.findAll().map(AuthorDto::fromDomainObject).collectList().block());
                x.setGenres(genreService.findAll().map(GenreDto::fromDomainObject).collectList().block());
                first[0] =false;
            }
            return Mono.just(x);
        }).switchIfEmpty(Flux.empty());
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/api/book/{id}")
    public Mono<ResponseEntity<BookDto>> getBook(@PathVariable("id") String id) {
        if (id == null || id.isEmpty() || id.equals("0")) {
            BookDto newBookDto = new BookDto("0", "", null, null, null, null);
            return Mono.just(newBookDto).flatMap(bookDto -> {
                   authorService.findAll().map(AuthorDto::fromDomainObject).collectList().subscribe(result -> bookDto.setAuthors(result));
                   genreService.findAll().map(GenreDto::fromDomainObject).collectList().subscribe(result -> bookDto.setGenres(result));
                   return Mono.just(bookDto);
                }).map(ResponseEntity::ok);
        } else {
            return bookService.findById(id).map(BookDto::fromDomainObject).flatMap(bookDto -> {
                    authorService.findAll().map(AuthorDto::fromDomainObject).collectList().subscribe(result -> bookDto.setAuthors(result));
                    genreService.findAll().map(GenreDto::fromDomainObject).collectList().subscribe(result -> bookDto.setGenres(result));
                    return Mono.just(bookDto);
                }).map(ResponseEntity::ok).switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping("/api/book/{id}")
    public Mono<ResponseEntity<Void>> deleteBook(@PathVariable("id") String id) {
        return bookService.deleteById(id).map(ResponseEntity::ok).switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping("/api/book/{id}")
    public Mono<ResponseEntity<BookDto>> editBook(@PathVariable("id") String id, @RequestBody BookDto bookDto) {
        if (bookDto.getId()!=null && !bookDto.getId().isEmpty() && !bookDto.getId().equals("0") &&
            id != null && !id.isEmpty() && id.equals(bookDto.getId())) {
            return bookService.update(id, bookDto.getTitle(), bookDto.getAuthorId(), bookDto.getGenreId())
                    .map(BookDto::fromDomainObject).flatMap(book -> Mono.just(book))
                    .map(ResponseEntity::ok)
                    .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
        } else {
            return Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/api/book")
    public Mono<ResponseEntity<BookDto>> createBook(@RequestBody BookDto bookDto) {
       if (bookDto.getId()==null || bookDto.getId().isEmpty() || bookDto.getId().equals("0")) {
               return bookService.insert(bookDto.getTitle(), bookDto.getAuthorId(), bookDto.getGenreId())
                       .map(BookDto::fromDomainObject).flatMap(book -> Mono.just(book))
                       .map(ResponseEntity::ok)
                       .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
       } else {
           return Mono.just(new ResponseEntity<BookDto>(HttpStatus.BAD_REQUEST));
       }
    }
}

