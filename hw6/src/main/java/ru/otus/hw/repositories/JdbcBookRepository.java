package ru.otus.hw.repositories;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.*;
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
        return Optional.of(jdbcTemplate.queryForObject(sql, new BookRowMapper(), id));

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
        findById(id).orElseThrow(() -> new EntityNotFoundException("The book with id %d not found, it could not be deleted.".formatted(id)));;
        String sql ="DELETE FROM BOOKS B WHERE B.ID=?";
        int res=jdbcTemplate.update(sql, id);
        if (res <= 0)
            throw new EntityNotFoundException("Failed to delete the book %d".formatted(id));
    }

    private Book insert(Book book) {
        if (book == null)
            throw new IllegalArgumentException("Do not insert null book.");
        if (book.getAuthor() == null)
            throw new IllegalArgumentException("Do not add the book without author.");
        if (book.getGenre() == null)
            throw new IllegalArgumentException("Do not add the book without a genre.");
        var keyHolder = new GeneratedKeyHolder();
        String sql ="INSERT INTO BOOKS(TITLE, AUTHOR_ID, GENRE_ID) VALUES (?, ?, ?)";
        jdbcTemplate.update(connection->{
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, book.getTitle());
            ps.setLong(2, book.getAuthor().getId());
            ps.setLong(3, book.getGenre().getId());
            return ps;}, keyHolder);
        Long id = keyHolder.getKeyAs(Long.class);
        if (id != null && id > 0) {
            book.setId(id);
            return book;
        } else
            throw new EntityNotFoundException("Failed to add a book %d".formatted(book.getTitle()));
    }

    private Book update(Book book) {
        if (book == null)
            throw new IllegalArgumentException("Do not update null book.");
        if (book.getAuthor() == null)
            throw new IllegalArgumentException("Do not renew the book without author.");
        if (book.getGenre() == null)
            throw new IllegalArgumentException("Do not renew the book without a genre.");
        //...
        // Выбросить EntityNotFoundException если не обновлено ни одной записи в БД
        findById(book.getId()).orElseThrow(() -> new EntityNotFoundException("Book with id %d not found, it couldn't be updated.".formatted(book.getId())));;
        String sql ="UPDATE BOOKS B SET B.TITLE=?, B.AUTHOR_ID=?, B.GENRE_ID=? WHERE B.ID=?";
        int res = jdbcTemplate.update(sql, book.getTitle(), book.getAuthor().getId(), book.getGenre().getId(), book.getId());
        if (res > 0) {
            return book;
        } else {
            throw new EntityNotFoundException("Failed to update a book %d".formatted(book.getId()));
        }
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
            book.setAuthorId(author.getId());
            book.setAuthor(author);
            Genre genre = new Genre();
            genre.setId(rs.getLong("GID"));
            genre.setName(rs.getString("NAME"));
            book.setGenreId(genre.getId());
            book.setGenre(genre);
            book.setRemarks(new ArrayList());
            return book;
        }
    }
}
