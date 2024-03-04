package ru.otus.hw.changelogs.test;

import lombok.*;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.entities.Author;
import ru.otus.hw.models.entities.Book;
import ru.otus.hw.models.entities.Genre;
import ru.otus.hw.models.entities.Remark;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AllEntitiesModelImpl implements AllEntitiesModel {
    private Map<Integer, List<Author>> authors;

    private Map<Integer, List<Genre>> genres;

    private Map<Integer, List<Remark>> remarks;

    private Map<Integer, List<Book>> books;

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
}
