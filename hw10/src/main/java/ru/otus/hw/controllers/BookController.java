package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.models.entities.Author;
import ru.otus.hw.models.entities.Book;
import ru.otus.hw.models.entities.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private static MultiValueMap<String, String> header = new LinkedMultiValueMap<>();

    static MultiValueMap<String, String> getHeader() {
        if (header.isEmpty()) {
            header.add("Access-Control-Allow-Origin", "*");
            header.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, PATCH, DELETE");
            header.add("Access-Control-Allow-Headers", "Origin,Content-Type,X-Requested-With,Accept,Authorization");
        }
        return header;
    }

    @GetMapping("/api/book")
    public ResponseEntity<List<BookDto>> bookList() {
        List<BookDto> books = bookService.findAll().stream().map(BookDto::fromDomainObject).toList();
        if (books != null && !books.isEmpty()) {
            List<AuthorDto> authorsDto = authorService.findAll().stream().map(AuthorDto::fromDomainObject).toList();
            List<GenreDto> genresDto = genreService.findAll().stream().map(GenreDto::fromDomainObject).toList();
            books.get(0).setAuthors(authorsDto);
            books.get(0).setGenres(genresDto);
        } else
            return new ResponseEntity<List<BookDto>>(null, getHeader(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<List<BookDto>>(books, getHeader(), HttpStatus.OK);
    }

    @GetMapping("/api/book/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable("id") String id) {
        BookDto bookDto;
        List<AuthorDto> authors = authorService.findAll().stream().map(AuthorDto::fromDomainObject).toList();
        List<GenreDto> genres = genreService.findAll().stream().map(GenreDto::fromDomainObject).toList();
        if (id != null && !id.isEmpty() && !id.equals("0")) {
            try {
                Book book = bookService.findById(id).get();
                bookDto = new BookDto(book.getId(), book.getTitle(), book.getAuthor().getId(), book.getGenre().getId(), authors, genres);
            } catch (NoSuchElementException e) {
                return new ResponseEntity<BookDto>(null, getHeader(), HttpStatus.NOT_FOUND);
            }
        } else {
            bookDto = new BookDto("0", "", null, null, authors, genres);
        }
        return new ResponseEntity<BookDto>(bookDto, getHeader(), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping("/api/book/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable("id") String id) {
        if (id != null && !id.isEmpty() && !id.equals("0")) {
            bookService.deleteById(id);
            return new ResponseEntity<>(null, getHeader(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, getHeader(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/api/book/{id}")
    public ResponseEntity<BookDto> editBook(@PathVariable("id") String id, @Valid @ModelAttribute("bookDto") BookDto bookDto) {
        Book editBook;
        if (bookDto.getId()!=null && !bookDto.getId().isEmpty() && !bookDto.getId().equals("0") &&
            id != null && !id.isEmpty() && id.equals(bookDto.getId())) {
            editBook = bookService.update(id, bookDto.getTitle(), bookDto.getAuthorId(), bookDto.getGenreId());
            return new ResponseEntity<BookDto>(BookDto.fromDomainObject(editBook), getHeader(), HttpStatus.OK);
        } else {
            return new ResponseEntity<BookDto>(null, getHeader(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/api/book")
    public ResponseEntity<BookDto> createBook(@Valid @ModelAttribute("bookDto") BookDto bookDto) {
       if (bookDto.getId()==null || bookDto.getId().isEmpty() || bookDto.getId().equals("0")) {
           Book newBook = bookService.insert(bookDto.getTitle(), bookDto.getAuthorId(), bookDto.getGenreId());
           return new ResponseEntity<BookDto>(BookDto.fromDomainObject(newBook), getHeader(), HttpStatus.CREATED);
       } else {
           return new ResponseEntity<BookDto>(null, getHeader(),HttpStatus.BAD_REQUEST);
       }
    }
}

