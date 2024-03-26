package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/")
    public String listPage(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "list";
    }

    @GetMapping("/book")
    public String editPage(@RequestParam("id") Long id, Model model) {
        BookDto bookDto;
        String sReturn = "edit";
        if (id != null && !id.equals(0L)) {
            Book book = bookService.findById(id).orElseThrow(NotFoundException::new);
            bookDto = new BookDto(book.getId(), book.getTitle(), book.getAuthor().getId().toString(), book.getGenre().getId().toString());
        } else {
            bookDto = new BookDto(0L, "", null, null);
            sReturn = "create";
        }
        model.addAttribute("bookDto", bookDto);
        List<Author> authors = authorService.findAll();
        model.addAttribute("authors", authors);
        List<Genre> genres = genreService.findAll();
        model.addAttribute("genres", genres);
        return sReturn;
    }

    @DeleteMapping("/book/{id}")
    public String deleteBook(@PathVariable("id") Long id, Model model) {
        if (id != null && !id.equals(0L)) {
            bookService.deleteById(id);
        }
        return "redirect:/";
    }

    @PutMapping("/book/{id}")
    public String editBook(@PathVariable("id") String id, @Valid @ModelAttribute("bookDto") BookDto bookDto,
                           BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            if (bookDto.getId()!=null && !bookDto.getId().equals(0L) &&
                id != null && id.equals(bookDto.getId().toString())) {
                bookService.update(bookDto.getId(), bookDto.getTitle(), Long.valueOf(bookDto.getAuthorId()), Long.valueOf(bookDto.getGenreId()));
                return "redirect:/";
            } else {
                return "create";
            }
        } else {
            List<Author> authors = authorService.findAll();
            model.addAttribute("authors", authors);
            List<Genre> genres = genreService.findAll();
            model.addAttribute("genres", genres);
            return "edit";
        }
    }

    @PostMapping("/book")
    public String createBook(@Valid @ModelAttribute("bookDto") BookDto bookDto,
                             BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            if (bookDto.getId()==null || bookDto.getId().equals(0L)) {
                bookService.insert(bookDto.getTitle(), Long.valueOf(bookDto.getAuthorId()), Long.valueOf(bookDto.getGenreId()));
            }
            return "redirect:/";
        } else {
            List<Author> authors = authorService.findAll();
            model.addAttribute("authors", authors);
            List<Genre> genres = genreService.findAll();
            model.addAttribute("genres", genres);
           return "create";
        }
    }
}

