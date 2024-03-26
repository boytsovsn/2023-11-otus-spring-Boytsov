package ru.otus.hw.models;

import jakarta.annotation.PostConstruct;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.entities.Author;
import ru.otus.hw.models.entities.Book;
import ru.otus.hw.models.entities.Genre;
import ru.otus.hw.models.entities.Remark;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.repositories.RemarkRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@ComponentScan({"ru.otus.hw.models", "ru.otus.hw.repositories"})
public class AllEntitiesModelImpl implements AllEntitiesModel {

    //Количество тестов и наборов данных для тестирования
    private final int testsCount = 4;

    private Map<Integer, List<Author>> authors;

    private Map<Integer, List<Genre>> genres;

    private Map<Integer, List<Remark>> remarks;

    private Map<Integer, List<Book>> books;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    RemarkRepository remarkRepository;

    @PostConstruct
    private void init() {
        deleteAll();
        initAuthor();
        initGenre();
        initBooks();
        initRemarks();
        initBookRemark();
    }

    @Override
    public void setAuthors(Map<Integer, List<Author>> authors) {
        this.authors = authors;
    }

    @Override
    public void setGenres(Map<Integer, List<Genre>> genres) {
        this.genres = genres;
    }

    @Override
    public void setRemarks(Map<Integer, List<Remark>> remarks) {
        this.remarks = remarks;
    }

    @Override
    public void setBooks(Map<Integer, List<Book>> books) {
        this.books = books;
    }

    @Override
    public Map<Integer, List<Author>> getAuthors() {
        return authors;
    }

    @Override
    public Map<Integer, List<Genre>> getGenres() {
        return genres;
    }

    @Override
    public Map<Integer, List<Remark>> getRemarks() {
        return remarks;
    }

    @Override
    public Map<Integer, List<Book>> getBooks() {
        return books;
    }

    public void deleteAll() {
        remarkRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        genreRepository.deleteAll();
    }

    public void initAuthor() {
        Map<Integer, List<Author>> authorsMap = new HashMap<Integer, List<Author>>();
        setAuthors(authorsMap);
        long j=1;
        for (Integer i = 0; i < testsCount; i++) {
            List<Author> authors = new ArrayList<>();
            authors.add(authorRepository.save(new Author(j++, "Author_1")));
            authors.add(authorRepository.save(new Author(j++,"Author_2")));
            authors.add(authorRepository.save(new Author(j++,"Author_3")));
            getAuthors().put(i, authors);
        }
    }

    public void initGenre() {
        Map<Integer, List<Genre>> genresMap = new HashMap<Integer, List<Genre>>();
        setGenres(genresMap);
        long j=1;
        for (Integer i = 0; i < testsCount; i++) {
            List<Genre> genres = new ArrayList<>();
            genres.add(genreRepository.save(new Genre(j++,"Genre_1")));
            genres.add(genreRepository.save(new Genre(j++,"Genre_2")));
            genres.add(genreRepository.save(new Genre(j++,"Genre_3")));
            getGenres().put(i, genres);
        }
    }

    public void initBooks() {
        Map<Integer, List<Book>> booksMap = new HashMap<Integer, List<Book>>();
        setBooks(booksMap);
        long j=1;
        for (Integer i = 0; i < testsCount; i++) {
            List<Book> books = new ArrayList<>();
            List<Author> authors = getAuthors().get(i);
            List<Genre> genres = getGenres().get(i);
            books.add(bookRepository.save(new Book(j++, "BookTitle_1", authors.get(0), genres.get(0), null)));
            books.add(bookRepository.save(new Book(j++, "BookTitle_2", authors.get(1), genres.get(1), null)));
            books.add(bookRepository.save(new Book(j++, "BookTitle_3", authors.get(2), genres.get(2), null)));
            getBooks().put(i, books);
        }
    }

    public void initRemarks() {
        Map<Integer, List<Remark>> remarksMap = new HashMap<Integer, List<Remark>>();
        setRemarks(remarksMap);
        long j=1;
        for (Integer i = 0; i < testsCount; i++) {
            List<Remark> remarks = new ArrayList<>();
            List<Book> books = getBooks().get(i);
            remarks.add(remarkRepository.save(new Remark(j++, "Remark_11", books.get(0))));
            remarks.add(remarkRepository.save(new Remark(j++, "Remark_21", books.get(1))));
            remarks.add(remarkRepository.save(new Remark(j++, "Remark_22", books.get(1))));
            remarks.add(remarkRepository.save(new Remark(j++, "Remark_31", books.get(2))));
            remarks.add(remarkRepository.save(new Remark(j++, "Remark_32", books.get(2))));
            remarks.add(remarkRepository.save(new Remark(j++, "Remark_33", books.get(2))));
            getRemarks().put(i, remarks);
        }
    }

    public void initBookRemark() {
        for (Integer i = 0; i < testsCount; i++) {
            List<Book> books = getBooks().get(i);
            List<Remark> remarks = getRemarks().get(i);
            for (Book book : books) {
                book.setRemarks(new ArrayList<Remark>());
            }
            books.get(0).getRemarks().add(remarks.get(0));
            books.get(1).getRemarks().add(remarks.get(1));
            books.get(1).getRemarks().add(remarks.get(2));
            books.get(2).getRemarks().add(remarks.get(3));
            books.get(2).getRemarks().add(remarks.get(4));
            books.get(2).getRemarks().add(remarks.get(5));
            getBooks().replace(i, books);
            for (Book book : books) {
                bookRepository.save(book);
            }
        }
    }

}
