package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.otus.hw.models.entities.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends BookRepositoryCustom, CrudRepository<Book, Long> {

    List<Book> findAll();

    Optional<Book> findById(Long id);

    Book save(Book book);

    void resetSequence();
}
