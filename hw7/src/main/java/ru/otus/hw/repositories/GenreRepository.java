package ru.otus.hw.repositories;

import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends GenreRepositoryData {
    List<Genre> findAll();

    Optional<Genre> findById(long id);
}
