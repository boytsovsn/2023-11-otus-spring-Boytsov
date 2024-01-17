package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class JpaBookRepository implements BookRepository {

    @PersistenceContext
    private final EntityManager em;

    public JpaBookRepository(EntityManager em) { this.em = em;}

    @Override
    public Optional<Book> findById(long id) {
        var book = em.find(Book.class, id);
        if (book!=null) {
            var author = em.find(Author.class, book.getAuthorId());
            book.setAuthor(author);
            var genre = em.find(Genre.class, book.getGenreId());
            book.setGenre(genre);
            book.getRemarks().size();
        }
        return Optional.ofNullable(book);
    }

    @Override
    public List<Book> findAll() {
        var query = em.createQuery("select distinct b from Book b left join fetch b.author left join fetch b.genre left join fetch b.remarks", Book.class);
        return query.getResultList();
    }

    @Override
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

    @Override
    public void deleteById(long id) {
        Book book = em.find(Book.class, id);
        if (book != null) {
            em.remove(book);
        }
    }
}
