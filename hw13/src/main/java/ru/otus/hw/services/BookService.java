package ru.otus.hw.services;

import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.otus.hw.models.entities.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    @PostAuthorize("hasPermission(returnObject, 'READ')")
    Book findById(Long id);

    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Book> findAll();

    Book insert(String title, Long authorId, Long genreId);

    Book update(Long id, String title, Long authorId, Long genreId);

    @PreAuthorize("hasPermission(#book, 'WRITE')")
    Book update(@Param("book") Book book);

    @PreAuthorize("hasPermission(#book, 'DELETE')")
    void delete(Book book);

    void deleteById(Long id);
}
