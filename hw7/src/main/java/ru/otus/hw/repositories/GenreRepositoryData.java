package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

@Repository
public interface GenreRepositoryData extends CrudRepository<Genre, Long> {

}
