package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;


public interface AuthorRepository extends CrudRepository<Author, Long> {

    List<Author> findAll();
}
