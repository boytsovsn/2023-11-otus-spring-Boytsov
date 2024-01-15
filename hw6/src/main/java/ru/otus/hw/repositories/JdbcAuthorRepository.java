package ru.otus.hw.repositories;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class JdbcAuthorRepository implements AuthorRepository {

    final private JdbcTemplate jdbcTemplate;

    @Override
    public List<Author> findAll() {
        String sql = "SELECT A.ID, A.FULL_NAME FROM AUTHORS A";
        return jdbcTemplate.query(sql, new AuthorRowMapper());
    }

    @Override
    public Optional<Author> findById(long id) {
        String sql = "SELECT A.ID, A.FULL_NAME FROM AUTHORS A WHERE A.ID = ?";
        return Optional.of(jdbcTemplate.queryForObject(sql, new Object[]{id}, new AuthorRowMapper()));
    }

    private static class AuthorRowMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            Author author = new Author();
            author.setId(rs.getLong("ID"));
            author.setFullName(rs.getString("FULL_NAME"));
            return author;
        }
    }
}
