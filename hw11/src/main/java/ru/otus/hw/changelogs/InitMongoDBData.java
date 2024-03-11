package ru.otus.hw.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.reactivestreams.client.MongoDatabase;
import ru.otus.hw.domain.entities.Author;
import ru.otus.hw.domain.entities.Book;
import ru.otus.hw.domain.entities.Genre;
import ru.otus.hw.domain.entities.Remark;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.GenreRepository;
import ru.otus.hw.repository.RemarkRepository;

import java.util.ArrayList;
import java.util.List;

@ChangeLog(order = "001")
public class InitMongoDBData {

    private List<Author> authors;

    private List<Genre> genres;

    private List<Remark> remarks;

    private List<Book> books;

//    @ChangeSet(order = "000", id = "dropDB", author = "boytsov", runAlways = true)
//    public void dropDB(MongoDatabase db) {
//        db.drop();
//    }
//
//    @ChangeSet(order = "001", id = "initAuthor", author = "boytsov", runAlways = true)
//    public void initAuthor(AuthorRepository repository) {
//        authors = new ArrayList<>();
//        repository.save(new Author("Конан Дойл")).subscribe(result->{authors.add(result);});
//        repository.save(new Author("Агата Кристи")).subscribe(result->{authors.add(result);});;
//        repository.save(new Author("Александр Пушкин")).subscribe(result->{authors.add(result);});;
//    }
//
//    @ChangeSet(order = "002", id = "initGenre", author = "boytsov", runAlways = true)
//    public void initGenre(GenreRepository repository) {
//        genres = new ArrayList<>();
//        repository.save(new Genre("Детектив")).subscribe(result->{genres.add(result);});
//        repository.save(new Genre("Роман"))   .subscribe(result->{genres.add(result);});
//        repository.save(new Genre("Поэма"))   .subscribe(result->{genres.add(result);});
//    }
//
//    @ChangeSet(order = "003", id = "initBooks", author = "boytsov", runAlways = true)
//    public void initBooks(BookRepository repository) {
//        books = new ArrayList<>();
//        repository.save(new Book("Шерлок Холмс", authors.get(0), genres.get(0))     ).subscribe(result->{books.add(result);});
//        repository.save(new Book("9 негритят", authors.get(1), genres.get(0))       ).subscribe(result->{books.add(result);});
//        repository.save(new Book("Капитанская дочка", authors.get(2), genres.get(1))).subscribe(result->{books.add(result);});
//    }
//
//    @ChangeSet(order = "004", id = "initRemarks", author = "boytsov", runAlways = true)
//    public void initRemarks(RemarkRepository repository) {
//        remarks = new ArrayList<>();
//        repository.save(new Remark("Круто!!!", books.get(0)) ).subscribe(result->{remarks.add(result);});
//        repository.save(new Remark("Не очень.", books.get(0))).subscribe(result->{remarks.add(result);});
//        repository.save(new Remark("Скучно.", books.get(1))  ).subscribe(result->{remarks.add(result);});
//        repository.save(new Remark("Интересно", books.get(2))).subscribe(result->{remarks.add(result);});
//        repository.save(new Remark("Классика", books.get(2)) ).subscribe(result->{remarks.add(result);});
//        repository.save(new Remark("Долго", books.get(2))    ).subscribe(result->{remarks.add(result);});
//    }
//
//    @ChangeSet(order = "005", id = "initBookRemark", author = "boytsov", runAlways = true)
//    public void initBookRemark(BookRepository repository) {
//        for (Book book : books) {
//            book.setRemarks(new ArrayList<Remark>());
//        }
//        books.get(0).getRemarks().add(remarks.get(0));
//        books.get(0).getRemarks().add(remarks.get(1));
//        books.get(1).getRemarks().add(remarks.get(2));
//        books.get(2).getRemarks().add(remarks.get(3));
//        books.get(2).getRemarks().add(remarks.get(4));
//        books.get(2).getRemarks().add(remarks.get(5));
//        for (Book book : books) {
//            repository.save(book);
//        }
//    }
}
