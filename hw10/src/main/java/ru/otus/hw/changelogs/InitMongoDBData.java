package ru.otus.hw.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.entities.Author;
import ru.otus.hw.models.entities.Book;
import ru.otus.hw.models.entities.Genre;
import ru.otus.hw.models.entities.Remark;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.repositories.RemarkRepository;

import java.util.ArrayList;
import java.util.List;

@ChangeLog(order = "001")
public class InitMongoDBData {

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
        authors.add(repository.save(new Author("Конан Дойл")));
        authors.add(repository.save(new Author("Агата Кристи")));
        authors.add(repository.save(new Author("Александр Пушкин")));
    }

    @ChangeSet(order = "002", id = "initGenre", author = "boytsov", runAlways = true)
    public void initGenre(GenreRepository repository) {
        genres = new ArrayList<>();
        genres.add(repository.save(new Genre("Детектив")));
        genres.add(repository.save(new Genre("Роман")));
        genres.add(repository.save(new Genre("Поэма")));
    }

    @ChangeSet(order = "003", id = "initBooks", author = "boytsov", runAlways = true)
    public void initBooks(BookRepository repository) {
        books = new ArrayList<>();
        books.add(repository.save(new Book("Шерлок Холмс", authors.get(0), genres.get(0))));
        books.add(repository.save(new Book("9 негритят", authors.get(1), genres.get(0))));
        books.add(repository.save(new Book("Капитанская дочка", authors.get(2), genres.get(1))));
    }

    @ChangeSet(order = "004", id = "initRemarks", author = "boytsov", runAlways = true)
    public void initRemarks(RemarkRepository repository) {
        remarks = new ArrayList<>();
        remarks.add(repository.save(new Remark("Круто!!!", books.get(0))));
        remarks.add(repository.save(new Remark("Не очень.", books.get(0))));
        remarks.add(repository.save(new Remark("Скучно.", books.get(1))));
        remarks.add(repository.save(new Remark("Интересно", books.get(2))));
        remarks.add(repository.save(new Remark("Классика", books.get(2))));
        remarks.add(repository.save(new Remark("Долго", books.get(2))));
    }

    @ChangeSet(order = "005", id = "initBookRemark", author = "boytsov", runAlways = true)
    public void initBookRemark(BookRepository repository) {
        for (Book book : books) {
            book.setRemarks(new ArrayList<Remark>());
        }
        books.get(0).getRemarks().add(remarks.get(0));
        books.get(0).getRemarks().add(remarks.get(1));
        books.get(1).getRemarks().add(remarks.get(2));
        books.get(2).getRemarks().add(remarks.get(3));
        books.get(2).getRemarks().add(remarks.get(4));
        books.get(2).getRemarks().add(remarks.get(5));
        for (Book book : books) {
            repository.save(book);
        }
    }
}
