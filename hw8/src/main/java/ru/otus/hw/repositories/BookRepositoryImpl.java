package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryCustom {

//    @PersistenceContext
//    private final EntityManager em;
//
    @Override
    public List<Book> findAll() {
//        EntityGraph<?> entityGraph = em.getEntityGraph("book-entity-graph");
//        var query = em.createQuery("select distinct b from Book b left join fetch b.remarks", Book.class);
//        query.setHint(FETCH.getKey(), entityGraph);
//        return query.getResultList();
        return new ArrayList<>();
    }
}
