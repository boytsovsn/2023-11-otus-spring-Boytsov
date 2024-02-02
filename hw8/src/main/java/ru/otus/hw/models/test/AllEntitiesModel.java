package ru.otus.hw.models.test;

import ru.otus.hw.models.entities.Author;
import ru.otus.hw.models.entities.Book;
import ru.otus.hw.models.entities.Genre;
import ru.otus.hw.models.entities.Remark;

import java.util.List;

public interface AllEntitiesModel {
    void setAuthors(List<Author> authors);

    void setGenres(List<Genre> genres);

    void setRemarks(List<Remark> remarks);

    void setBooks(List<Book> books);

    List<Author> getAuthors();

    List<Genre> getGenres();

    List<Remark> getRemarks();

    List<Book> getBooks();
}
