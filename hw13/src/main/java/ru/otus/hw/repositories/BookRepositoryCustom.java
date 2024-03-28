package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.Query;
import ru.otus.hw.models.entities.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepositoryCustom {

    List<Book> findAll();


    Optional<Book> findById(Long id);

    void resetSequence();
}
