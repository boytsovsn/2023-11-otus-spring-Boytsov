package ru.otus.hw.models.test;

import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.entities.Author;
import ru.otus.hw.models.entities.Book;
import ru.otus.hw.models.entities.Genre;
import ru.otus.hw.models.entities.Remark;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AllEntitiesModelImpl implements AllEntitiesModel {
    private List<Author> authors;

    private List<Genre> genres;

    private List<Remark> remarks;

    private List<Book> books;

    @Override
    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    @Override
    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    @Override
    public void setRemarks(List<Remark> remarks) {
        this.remarks = remarks;
    }

    @Override
    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public List<Author> getAuthors() {
        return authors;
    }

    @Override
    public List<Genre> getGenres() {
        return genres;
    }

    @Override
    public List<Remark> getRemarks() {
        return remarks;
    }

    @Override
    public List<Book> getBooks() {
        return books;
    }
}
