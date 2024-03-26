package ru.otus.hw.services;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import ru.otus.hw.models.entities.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    @PostAuthorize("hasPermission(returnObject, 'READ')")
    Optional<Book> findById(Long id);

    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Book> findAll();

    Book insert(String title, Long authorId, Long genreId);

    Book update(Long id, String title, Long authorId, Long genreId);

    void deleteById(Long id);
}
