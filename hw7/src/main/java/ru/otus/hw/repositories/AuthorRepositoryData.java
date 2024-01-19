package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

@Repository
public interface AuthorRepositoryData extends CrudRepository<Author, Long> {

}
