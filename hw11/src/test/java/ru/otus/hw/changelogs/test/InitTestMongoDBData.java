package ru.otus.hw.changelogs.test;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.hw.domain.entities.Author;
import ru.otus.hw.domain.entities.Book;
import ru.otus.hw.domain.entities.Genre;
import ru.otus.hw.domain.entities.Remark;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.GenreRepository;
import ru.otus.hw.repository.RemarkRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ChangeLog(order = "001")
public class InitTestMongoDBData {

    //Количество тестов и наборов данных для тестирования
    private final int testsCount = 4;

    @ChangeSet(order = "000", id = "dropDB", author = "boytsov", runAlways = true)
    public void dropDB(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "001", id = "initAuthor", author = "boytsov", runAlways = true)
    public void initAuthor(AuthorRepository repository, @Autowired AllEntitiesModel allEntitiesModelImpl) {
        Map<Integer, List<Author>> authorsMap = new HashMap<Integer, List<Author>>();
        allEntitiesModelImpl.setAuthors(authorsMap);
        for (Integer i = 0; i < testsCount; i++) {
            List<Author> authors = new ArrayList<>();
            repository.save(new Author("Author_1")).subscribe(result->{authors.add(result);});
            repository.save(new Author("Author_2")).subscribe(result->{authors.add(result);});
            repository.save(new Author("Author_3")).subscribe(result->{authors.add(result);});
            allEntitiesModelImpl.getAuthors().put(i, authors);
        }
    }

    @ChangeSet(order = "002", id = "initGenre", author = "boytsov", runAlways = true)
    public void initGenre(GenreRepository repository, @Autowired  AllEntitiesModel allEntitiesModelImpl) {
        Map<Integer, List<Genre>> genresMap = new HashMap<Integer, List<Genre>>();
        allEntitiesModelImpl.setGenres(genresMap);
        for (Integer i = 0; i < testsCount; i++) {
            List<Genre> genres = new ArrayList<>();
            repository.save(new Genre("Genre_1")).subscribe(result->{genres.add(result);});
            repository.save(new Genre("Genre_2")).subscribe(result->{genres.add(result);});
            repository.save(new Genre("Genre_3")).subscribe(result->{genres.add(result);});
            allEntitiesModelImpl.getGenres().put(i, genres);
        }
    }

    @ChangeSet(order = "003", id = "initBooks", author = "boytsov", runAlways = true)
    public void initBooks(BookRepository repository, @Autowired  AllEntitiesModel allEntitiesModelImpl) {
        Map<Integer, List<Book>> booksMap = new HashMap<Integer, List<Book>>();
        allEntitiesModelImpl.setBooks(booksMap);
        for (Integer i = 0; i < testsCount; i++) {
            List<Book> books = new ArrayList<>();
            List<Author> authors = allEntitiesModelImpl.getAuthors().get(i);
            List<Genre> genres = allEntitiesModelImpl.getGenres().get(i);
            repository.save(new Book("BookTitle_1", authors.get(0), genres.get(0))).subscribe(result->{books.add(result);});
            repository.save(new Book("BookTitle_2", authors.get(1), genres.get(1))).subscribe(result->{books.add(result);});
            repository.save(new Book("BookTitle_3", authors.get(2), genres.get(2))).subscribe(result->{books.add(result);});
            allEntitiesModelImpl.getBooks().put(i, books);
        }
    }

    @ChangeSet(order = "004", id = "initRemarks", author = "boytsov", runAlways = true)
    public void initRemarks(RemarkRepository repository, @Autowired  AllEntitiesModel allEntitiesModelImpl) {
        Map<Integer, List<Remark>> remarksMap = new HashMap<Integer, List<Remark>>();
        allEntitiesModelImpl.setRemarks(remarksMap);
        for (Integer i = 0; i < testsCount; i++) {
            List<Remark> remarks = new ArrayList<>();
            List<Book> books = allEntitiesModelImpl.getBooks().get(i);
            repository.save(new Remark("Remark_11", books.get(0))).subscribe(result->{remarks.add(result);});
            repository.save(new Remark("Remark_21", books.get(1))).subscribe(result->{remarks.add(result);});
            repository.save(new Remark("Remark_22", books.get(1))).subscribe(result->{remarks.add(result);});
            repository.save(new Remark("Remark_31", books.get(2))).subscribe(result->{remarks.add(result);});
            repository.save(new Remark("Remark_32", books.get(2))).subscribe(result->{remarks.add(result);});
            repository.save(new Remark("Remark_33", books.get(2))).subscribe(result->{remarks.add(result);});
            allEntitiesModelImpl.getRemarks().put(i, remarks);
        }
    }

    @ChangeSet(order = "005", id = "initBookRemark", author = "boytsov", runAlways = true)
    public void initBookRemark(BookRepository repository, @Autowired  AllEntitiesModel allEntitiesModelImpl) {
        for (Integer i = 0; i < testsCount; i++) {
            List<Book> books = allEntitiesModelImpl.getBooks().get(i);
            List<Remark> remarks = allEntitiesModelImpl.getRemarks().get(i);
            for (Book book : books) {
                book.setRemarks(new ArrayList<Remark>());
            }
            books.get(0).getRemarks().add(remarks.get(0));
            books.get(1).getRemarks().add(remarks.get(1));
            books.get(1).getRemarks().add(remarks.get(2));
            books.get(2).getRemarks().add(remarks.get(3));
            books.get(2).getRemarks().add(remarks.get(4));
            books.get(2).getRemarks().add(remarks.get(5));
            for (Book book : books) {
                repository.save(book);
            }
            allEntitiesModelImpl.getBooks().replace(i, books);
        }
    }
}
