package ru.otus.hw.repositories;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class GenreRepositoryJdbc implements GenreRepository {

    final private JdbcTemplate jdbcTemplate;
    @Override
    public List<Genre> findAll() {
        String sql = "SELECT A.ID, A.NAME FROM GENRES A";
        return jdbcTemplate.query(sql, new GenreRowMapper());
    }

    @Override
    public Optional<Genre> findById(long id) {
        String sql = "SELECT A.ID, A.NAME FROM GENRES A WHERE A.ID = ?";
        return Optional.of(jdbcTemplate.queryForObject(sql, new Object[]{id}, new GenreRowMapper()));
    }

    private static class GenreRowMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            Genre genre = new Genre();
            genre.setId(rs.getLong("ID"));
            genre.setName(rs.getString("NAME"));
            return genre;
        }
    }
}
