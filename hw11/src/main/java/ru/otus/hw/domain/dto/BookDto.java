package ru.otus.hw.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.domain.entities.Author;
import ru.otus.hw.domain.entities.Book;
import ru.otus.hw.domain.entities.Genre;

import java.util.List;

@Data
@AllArgsConstructor
public class BookDto {

    private String id;

    private String title;

    private String authorId;

    private String genreId;

    private List<AuthorDto> authors;

    private List<GenreDto> genres;

    public Book toDomainObject(){
        return new Book(id, title, authorId, genreId, null);
    }

    public static BookDto fromDomainObject(Book book) {
        return new BookDto(book.getId(), book.getTitle(), book.getAuthorId(), book.getGenreId(), null, null);
    }
}
