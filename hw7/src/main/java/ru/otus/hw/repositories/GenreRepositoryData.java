package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ru.otus.hw.models.Genre;

@NoRepositoryBean
public interface GenreRepositoryData extends JpaRepository<Genre, Long> {

}
