package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Component
public class BookRepositoryImpl /*implements BookRepository*/ {

    @PersistenceContext
    private final EntityManager em;

    public BookRepositoryImpl(EntityManager em) { this.em = em;}

//    @Override
    public Optional<Book> findById(long id) {
        var book = em.find(Book.class, id);
        return Optional.ofNullable(book);
    }

//    @Override
    public List<Book> findAll() {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-entity-graph");
        var query = em.createQuery("select distinct b from Book b left join fetch b.remarks", Book.class);
        query.setHint(FETCH.getKey(), entityGraph);
        return query.getResultList();
    }

//    @Override
    public Book save(Book book) {
        if (book != null) {
            if (book.getId() == 0) {
                em.persist(book);
            } else {
                em.merge(book);
            }
        }
        return book;
    }

//    @Override
    public void deleteById(long id) {
        Book book = em.find(Book.class, id);
        if (book != null) {
            em.remove(book);
        }
    }
}
