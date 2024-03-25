package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.entities.Author;

import java.util.List;


public interface AuthorRepository extends CrudRepository<Author, Long> {

    List<Author> findAll();
}
