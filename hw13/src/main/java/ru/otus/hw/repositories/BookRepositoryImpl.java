package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.entities.Book;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Component
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryCustom {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public List<Book> findAll() {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-entity-graph");
        var query = em.createQuery("select distinct b from Book b left join fetch b.remarks order by b.id", Book.class);
        query.setHint(FETCH.getKey(), entityGraph);
        return query.getResultList();
    }

    @Override
    public Optional<Book> findById(Long Id) {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-entity-graph");
        var query = em.createQuery("select distinct b from Book b left join fetch b.remarks where b.id = :id", Book.class);
        query.setHint(FETCH.getKey(), entityGraph);
        query.setParameter("id", Id);
        try {
            Book res = query.getSingleResult();
            return Optional.of(res);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public void resetSequence() {
        String queryStr = "ALTER SEQUENCE book_id_seq RESTART WITH 4";
        var query = em.createNativeQuery(queryStr).executeUpdate();
    }

}
