package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;


public interface GenreRepository extends CrudRepository<Genre, Long> {
    List<Genre> findAll();
}
