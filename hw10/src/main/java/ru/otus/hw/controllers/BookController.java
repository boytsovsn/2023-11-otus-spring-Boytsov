package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.entities.Author;
import ru.otus.hw.models.entities.Book;
import ru.otus.hw.models.entities.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;

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

    @GetMapping("/api/books")
    public ResponseEntity<List<BookDto>> listPage(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return new ResponseEntity<List<BookDto>>(books.stream().map(BookDto::fromDomainObject).toList(), getHeader(), HttpStatus.OK);
    }

    @GetMapping("/api/book")
    public String editPage(@RequestParam("id") String id, Model model) {
        BookDto bookDto;
        String sReturn = "edit";
        if (id != null && !id.isEmpty() && !id.equals("0")) {
            Book book = bookService.findById(id).orElseThrow(NotFoundException::new);
            bookDto = new BookDto(book.getId(), book.getTitle(), book.getAuthor().getId(), book.getGenre().getId());
        } else {
            bookDto = new BookDto("0", "", null, null);
            sReturn = "create";
        }
        model.addAttribute("bookDto", bookDto);
        List<Author> authors = authorService.findAll();
        model.addAttribute("authors", authors);
        List<Genre> genres = genreService.findAll();
        model.addAttribute("genres", genres);
        return sReturn;
    }

    @DeleteMapping("/api/book/{id}")
    public ResponseEntity<?> deleteBook(@RequestParam("id") String id) {
        if (id != null && !id.isEmpty() && !id.equals("0")) {
            bookService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/api/book/{id}")
    public ResponseEntity<BookDto> editBook(@RequestParam("id") String id, @RequestBody BookDto bookDto) {
        if (bookDto.getId()!=null && !bookDto.getId().isEmpty() && !bookDto.getId().equals("0") &&
            id != null && !id.isEmpty() && id.equals(bookDto.getId())) {
            Book editBook = bookService.update(bookDto.getId(), bookDto.getTitle(), bookDto.getAuthorId(), bookDto.getGenreId());
            return new ResponseEntity<BookDto>(BookDto.fromDomainObject(editBook), HttpStatus.OK);
        } else {
            return new ResponseEntity<BookDto>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/api/book")
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto) {
       if (bookDto.getId()==null || bookDto.getId().isEmpty() || bookDto.getId().equals("0")) {
           Book newBook = bookService.insert(bookDto.getTitle(), bookDto.getAuthorId(), bookDto.getGenreId());
           return new ResponseEntity<BookDto>(BookDto.fromDomainObject(newBook), HttpStatus.CREATED);
       } else {
           return new ResponseEntity<BookDto>(HttpStatus.BAD_REQUEST);
       }
    }
}

