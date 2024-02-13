package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.entities.Book;
import ru.otus.hw.services.BookService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/")
    public String listPage(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "list";
    }

    @GetMapping("/edit")
    public String editPage(@RequestParam("id") String id, Model model) {
        Book book = bookService.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("book", book);
        return "edit";
    }

    @PostMapping("/edit")
    public String saveBook(@Valid @ModelAttribute("book") BookDto book,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "edit";
        }
        Book bookEntity = book.toDomainObject();
        bookService.update(bookEntity.getId(), bookEntity.getTitle(), bookEntity.getAuthor().getId(), bookEntity.getGenre().getId());
        return "redirect:/";
    }
}
