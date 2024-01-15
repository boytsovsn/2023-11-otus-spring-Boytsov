package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Репозиторий для Category должен")
@DataJpaTest
@Import(JpaGenreRepository.class)
@Transactional(propagation = Propagation.NEVER)
class JpaGenreRepositoryTest {
//
//    @Autowired
//    private GenreRepository jpaGenreRepository;
//
//    @Autowired
//    private TestEntityManager em;
//
//    @Test
//    void findAll() {
//    }
//
//    @Test
//    void findById() {
//    }
}