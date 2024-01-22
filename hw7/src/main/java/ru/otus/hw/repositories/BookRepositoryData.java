package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ru.otus.hw.models.Book;

@NoRepositoryBean
public interface BookRepositoryData extends JpaRepository<Book, Long> {

}
