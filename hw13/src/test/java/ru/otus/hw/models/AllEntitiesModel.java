package ru.otus.hw.models;

import ru.otus.hw.models.entities.Author;
import ru.otus.hw.models.entities.Book;
import ru.otus.hw.models.entities.Genre;
import ru.otus.hw.models.entities.Remark;

import java.util.List;
import java.util.Map;

public interface AllEntitiesModel {
    void setAuthors(Map<Integer, List<Author>> authors);

    void setGenres (Map<Integer, List<Genre>> genres);

    void setRemarks(Map<Integer, List<Remark>> remarks);

    void setBooks  (Map<Integer, List<Book>> books);

    Map<Integer, List<Author>> getAuthors();

    Map<Integer, List<Genre>>  getGenres();

    Map<Integer, List<Remark>> getRemarks();

    Map<Integer, List<Book>>   getBooks();
}
