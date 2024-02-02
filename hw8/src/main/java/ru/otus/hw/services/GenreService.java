package ru.otus.hw.services;

import ru.otus.hw.models.entities.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    List<Genre> findAll();

    Optional<Genre> findById(String id);
}
