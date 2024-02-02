package ru.otus.hw.changelogs.test;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.hw.models.entities.Author;
import ru.otus.hw.models.entities.Book;
import ru.otus.hw.models.entities.Genre;
import ru.otus.hw.models.entities.Remark;
import ru.otus.hw.models.test.AllEntitiesModel;
import ru.otus.hw.models.test.AllEntitiesModelImpl;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.repositories.RemarkRepository;

import java.util.ArrayList;
import java.util.List;

@ChangeLog(order = "001")
public class InitTestMongoDBData {

    @Autowired
    private final AllEntitiesModel allEntitiesModelImpl;

    public InitTestMongoDBData() {
        this.allEntitiesModelImpl = new AllEntitiesModelImpl();
    }

    public InitTestMongoDBData(AllEntitiesModel allEntitiesModelImpl) {
        this.allEntitiesModelImpl = allEntitiesModelImpl;
    }

    @ChangeSet(order = "000", id = "dropDB", author = "boytsov", runAlways = true)
    public void dropDB(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "001", id = "initAuthor", author = "boytsov", runAlways = true)
    public void initAuthor(AuthorRepository repository) {
        List<Author> authors = new ArrayList<>();
        authors.add(repository.save(new Author("Author_1")));
        authors.add(repository.save(new Author("Author_2")));
        authors.add(repository.save(new Author("Author_3")));
        allEntitiesModelImpl.setAuthors(authors);
    }

    @ChangeSet(order = "002", id = "initGenre", author = "boytsov", runAlways = true)
    public void initGenre(GenreRepository repository) {
        List<Genre> genres = new ArrayList<>();
        genres.add(repository.save(new Genre("Genre_1")));
        genres.add(repository.save(new Genre("Genre_2")));
        genres.add(repository.save(new Genre("Genre_3")));
        allEntitiesModelImpl.setGenres(genres);
    }

    @ChangeSet(order = "003", id = "initBooks", author = "boytsov", runAlways = true)
    public void initBooks(BookRepository repository) {
        List<Book> books = new ArrayList<>();
        List<Author> authors = allEntitiesModelImpl.getAuthors();
        List<Genre> genres = allEntitiesModelImpl.getGenres();
        books.add(repository.save(new Book("BookTitle_1", authors.get(0), genres.get(0))));
        books.add(repository.save(new Book("BookTitle_2", authors.get(1), genres.get(1))));
        books.add(repository.save(new Book("BookTitle_3", authors.get(2), genres.get(2))));
        allEntitiesModelImpl.setBooks(books);
    }

    @ChangeSet(order = "004", id = "initRemarks", author = "boytsov", runAlways = true)
    public void initRemarks(RemarkRepository repository) {
        List<Remark> remarks = new ArrayList<>();
        List<Book> books = allEntitiesModelImpl.getBooks();
        remarks.add(repository.save(new Remark("Remark_11", books.get(0).getId())));
        remarks.add(repository.save(new Remark("Remark_21", books.get(1).getId())));
        remarks.add(repository.save(new Remark("Remark_22", books.get(1).getId())));
        remarks.add(repository.save(new Remark("Remark_31", books.get(2).getId())));
        remarks.add(repository.save(new Remark("Remark_32", books.get(2).getId())));
        remarks.add(repository.save(new Remark("Remark_33", books.get(2).getId())));
        allEntitiesModelImpl.setRemarks(remarks);
    }

    @ChangeSet(order = "005", id = "initBookRemark", author = "boytsov", runAlways = true)
    public void initBookRemark(BookRepository repository) {
        List<Book> books = allEntitiesModelImpl.getBooks();
        List<Remark> remarks = allEntitiesModelImpl.getRemarks();
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
            book = repository.save(book);
        }
        allEntitiesModelImpl.setBooks(books);
    }
}
