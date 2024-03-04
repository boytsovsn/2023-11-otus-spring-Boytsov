package ru.otus.spring.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.spring.domain.entities.Author;
import ru.otus.spring.domain.entities.Book;
import ru.otus.spring.domain.entities.Genre;

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
        return new Book(id, title, new Author(authorId, null), new Genre(genreId, null), null);
    }

    public static BookDto fromDomainObject(Book book) {
        return new BookDto(book.getId(), book.getTitle(), book.getAuthor().getId(), book.getGenre().getId(), null, null);
    }
}
