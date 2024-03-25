package ru.otus.hw.services;

import ru.otus.hw.models.entities.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    List<Author> findAll();

    Optional<Author> findById(Long id);
}
