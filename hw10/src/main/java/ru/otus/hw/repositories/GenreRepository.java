package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.entities.Genre;

import java.util.List;


public interface GenreRepository extends MongoRepository<Genre, String> {
    List<Genre> findAll();
}
