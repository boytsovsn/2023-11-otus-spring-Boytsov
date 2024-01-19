package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

@Repository
public interface BookRepositoryData extends CrudRepository<Book, Long> {

}
