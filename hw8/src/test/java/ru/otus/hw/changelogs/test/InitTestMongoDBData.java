package ru.otus.hw.changelogs.test;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.Remark;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.repositories.RemarkRepository;

import java.util.ArrayList;
import java.util.List;

@ChangeLog(order = "001")
public class InitTestMongoDBData {

    private List<Author> authors;

    private List<Genre> genres;

    private List<Remark> remarks;

    private List<Book> books;

    @ChangeSet(order = "000", id = "dropDB", author = "boytsov", runAlways = true)
    public void dropDB(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "001", id = "initAuthor", author = "boytsov", runAlways = true)
    public void initAuthor(AuthorRepository repository) {
        authors = new ArrayList<>();
        authors.add(repository.save(new Author("Author_1")));
        authors.add(repository.save(new Author("Author_2")));
        authors.add(repository.save(new Author("Author_3")));
    }

    @ChangeSet(order = "002", id = "initGenre", author = "boytsov", runAlways = true)
    public void initGenre(GenreRepository repository) {
        genres = new ArrayList<>();
        genres.add(repository.save(new Genre("Genre_1")));
        genres.add(repository.save(new Genre("Genre_2")));
        genres.add(repository.save(new Genre("Genre_3")));
    }

    @ChangeSet(order = "003", id = "initBooks", author = "boytsov", runAlways = true)
    public void initBooks(BookRepository repository) {
        books = new ArrayList<>();
        books.add(repository.save(new Book("BookTitle_1", authors.get(0), genres.get(0))));
        books.add(repository.save(new Book("BookTitle_2", authors.get(1), genres.get(1))));
        books.add(repository.save(new Book("BookTitle_3", authors.get(2), genres.get(2))));
    }

    @ChangeSet(order = "004", id = "initRemarks", author = "boytsov", runAlways = true)
    public void initRemarks(RemarkRepository repository) {
        remarks = new ArrayList<>();
        remarks.add(repository.save(new Remark("Remark_11", books.get(0).getId())));
        remarks.add(repository.save(new Remark("Remark_21", books.get(1).getId())));
        remarks.add(repository.save(new Remark("Remark_22", books.get(1).getId())));
        remarks.add(repository.save(new Remark("Remark_31", books.get(2).getId())));
        remarks.add(repository.save(new Remark("Remark_32", books.get(2).getId())));
        remarks.add(repository.save(new Remark("Remark_33", books.get(2).getId())));
    }

    @ChangeSet(order = "005", id = "initBookRemark", author = "boytsov", runAlways = true)
    public void initBookRemark(BookRepository repository) {
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
    }
}
