package ru.otus.hw;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.otus.hw.changelogs.test.AllEntitiesModel;
import ru.otus.hw.models.entities.Author;
import ru.otus.hw.models.entities.Book;
import ru.otus.hw.models.entities.Genre;
import ru.otus.hw.models.entities.Remark;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@DataMongoTest
@EnableConfigurationProperties
public class BaseTest {

    @Autowired
    protected AllEntitiesModel allEntitiesModelImpl;

    protected List<Author> dbAuthors;

    protected List<Genre> dbGenres;

    protected List<List<Remark>> dbRemarks;

    protected List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbRemarks = getDbRemarks();
        dbBooks = getDbBooks(dbAuthors, dbGenres, dbRemarks);
    }

    protected List<Remark> convertToFlatDbRemarks() {
        return dbRemarks.stream().flatMap(x->x.stream()).collect(Collectors.toList());
    }

    protected List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(allEntitiesModelImpl.getAuthors().get(id-1).getId(), "Author_" + id))
                .toList();
    }

    protected List<Genre> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Genre(allEntitiesModelImpl.getGenres().get(id-1).getId(), "Genre_" + id))
                .toList();
    }

    protected List<List<Remark>> getDbRemarks() {
        return IntStream.range(1, 4).boxed()
                .map(id -> IntStream.range(1, id+1).boxed()
                        .map(id1 ->{return new Remark(allEntitiesModelImpl.getRemarks().get(id*(id - 1)/2 + id1 - 1).getId(), "Remark_"+id+id1, allEntitiesModelImpl.getBooks().get(id - 1)); }).toList())
                .toList();
    }

    protected List<Book> getDbBooks(List<Author> _dbAuthors, List<Genre> _dbGenres, List<List<Remark>> _dbRemarks) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(allEntitiesModelImpl.getBooks().get(id-1).getId(), "BookTitle_" + id, _dbAuthors.get(id - 1), _dbGenres.get(id - 1), _dbRemarks.get(id - 1)))
                .toList();
    }

    protected List<Book> getDbBooks() {
        var authors = getDbAuthors();
        var genres = getDbGenres();
        var remarks = getDbRemarks();
        return getDbBooks(authors, genres, remarks);
    }
}
