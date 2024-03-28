package ru.otus.hw;

import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.hw.models.AllEntitiesModel;
import ru.otus.hw.models.entities.Author;
import ru.otus.hw.models.entities.Book;
import ru.otus.hw.models.entities.Genre;
import ru.otus.hw.models.entities.Remark;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BaseTest {

    @Autowired
    protected AllEntitiesModel allEntitiesModelImpl;

    protected List<Author> dbAuthors;

    protected List<Genre> dbGenres;

    protected List<List<Remark>> dbRemarks;

    protected List<Book> dbBooks;

    protected int testN;

    public void setUp(int testN) {
        allEntitiesModelImpl.init(testN);
        if (testN < allEntitiesModelImpl.getTestsCount()) {
            dbAuthors = getDbAuthors(testN);
            dbGenres = getDbGenres(testN);
            dbRemarks = getDbRemarks(testN, testN);
            dbBooks = getDbBooks(testN, dbAuthors, dbGenres, dbRemarks);
        } else if (testN == allEntitiesModelImpl.getTestsCount()) {   //тесты для ACL Security
            dbAuthors = allEntitiesModelImpl.getAuthors().get(testN);
            dbGenres = allEntitiesModelImpl.getGenres().get(testN);
            dbRemarks = getDbRemarks(testN, testN);
            dbBooks = getDbACLBooks(testN, dbAuthors, dbGenres, dbRemarks);
        }
    }

    protected List<Remark> convertToFlatDbRemarks() {
        return dbRemarks.stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    protected List<Author> getDbAuthors(int t) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(allEntitiesModelImpl.getAuthors().get(t).get(id-1).getId(), "Author_" + id))
                .toList();
    }

    protected List<Genre> getDbGenres(int t) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Genre(allEntitiesModelImpl.getGenres().get(t).get(id-1).getId(), "Genre_" + id))
                .toList();
    }

    protected List<List<Remark>> getDbRemarks(int t, int t1) {
        return IntStream.range(1, 4).boxed()
                .map(id -> IntStream.range(1, id+1).boxed()
                        .map(id1 -> new Remark(allEntitiesModelImpl.getRemarks().get(t).get(id*(id - 1)/2 + id1 - 1).getId(), "Remark_"+id+id1, allEntitiesModelImpl.getBooks().get(t1).get(id - 1))).toList())
                .toList();
    }

    protected List<Book> getDbBooks(int t, List<Author> _dbAuthors, List<Genre> _dbGenres, List<List<Remark>> _dbRemarks) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(allEntitiesModelImpl.getBooks().get(t).get(id-1).getId(), "BookTitle_" + id, _dbAuthors.get(id - 1), _dbGenres.get(id - 1), _dbRemarks.get(id - 1)))
                .toList();
    }

    protected List<Book> getDbACLBooks(int t, List<Author> _dbAuthors, List<Genre> _dbGenres, List<List<Remark>> _dbRemarks) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(allEntitiesModelImpl.getBooks().get(t).get(id-1).getId(), allEntitiesModelImpl.getBooks().get(t).get(id-1).getTitle(), _dbAuthors.get(id - 1), _dbGenres.get(id - 1), _dbRemarks.get(id - 1)))
                .toList();
    }

}
