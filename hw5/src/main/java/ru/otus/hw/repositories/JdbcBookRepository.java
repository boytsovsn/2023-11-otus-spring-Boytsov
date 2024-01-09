package ru.otus.hw.repositories;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class JdbcBookRepository implements BookRepository {
    final private JdbcTemplate jdbcTemplate;
    @Override
    public Optional<Book> findById(long id) {
        String sql = "SELECT B.ID, B.TITLE, A.ID AID, A.FULL_NAME, G.ID GID, G.NAME FROM BOOKS B, AUTHORS A, GENRES G WHERE B.AUTHOR_ID = A.ID AND B.GENRE_ID = G.ID AND B.ID = ?";
        return Optional.of(jdbcTemplate.queryForObject(sql, new Object[]{id}, new BookRowMapper()));

    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT B.ID, B.TITLE, A.ID AID, A.FULL_NAME, G.ID GID, G.NAME FROM BOOKS B, AUTHORS A, GENRES G WHERE B.AUTHOR_ID = A.ID AND B.GENRE_ID = G.ID";
        return jdbcTemplate.query(sql, new BookRowMapper());
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        //...
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        //...

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        //...
        // Выбросить EntityNotFoundException если не обновлено ни одной записи в БД
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Book book = new Book();
            book.setId(rs.getLong("ID"));
            book.setTitle(rs.getString("TITLE"));
            Author author = new Author();
            author.setId(rs.getLong("AID"));
            author.setFullName(rs.getString("FULL_NAME"));
            book.setAuthor(author);
            Genre genre = new Genre();
            genre.setId(rs.getLong("GID"));
            genre.setName(rs.getString("NAME"));
            book.setGenre(genre);
            return book;
        }
    }
}
