package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.entities.Book;

import java.util.List;

public interface BookRepository extends BookRepositoryCustom, CrudRepository<Book, Long> {

    List<Book> findAll();
}
