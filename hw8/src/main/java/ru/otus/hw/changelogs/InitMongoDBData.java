package ru.otus.hw.changelogs;

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
public class InitMongoDBData {

    private List<Author> authors;

    private List<Genre> genres;

    private List<Remark> remarks;

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

    @ChangeSet(order = "003", id = "initRemarks", author = "boytsov", runAlways = true)
    public void initRemarks(RemarkRepository repository) {
        remarks = new ArrayList<>();
        remarks.add(repository.save(new Remark("Круто!!!")));
        remarks.add(repository.save(new Remark("Не очень.")));
        remarks.add(repository.save(new Remark("Скучно.")));
    }

    @ChangeSet(order = "004", id = "initBooks", author = "boytsov", runAlways = true)
    public void initBooks(BookRepository repository) {
        var book = repository.save(new Book("Шерлок Холмс", authors.get(0), genres.get(0), remarks.get(0), remarks.get(2)));
        book = repository.save(new Book("9 негритят", authors.get(1), genres.get(0), remarks.get(0), remarks.get(1)));
        book = repository.save(new Book("Капитанская дочка", authors.get(2), genres.get(1), remarks.get(0)));
    }
}
