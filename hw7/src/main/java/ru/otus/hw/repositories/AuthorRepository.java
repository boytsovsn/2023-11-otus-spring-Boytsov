package ru.otus.hw.repositories;

import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends AuthorRepositoryData {
    List<Author> findAll();

    Optional<Author> findById(long id);
}
