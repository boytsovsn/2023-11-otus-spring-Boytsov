package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.entities.Genre;

import java.util.List;


public interface GenreRepository extends CrudRepository<Genre, Long> {
    List<Genre> findAll();
}
