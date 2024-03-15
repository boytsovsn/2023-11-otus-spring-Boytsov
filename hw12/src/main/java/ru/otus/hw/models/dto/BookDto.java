package ru.otus.hw.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.entities.Author;
import ru.otus.hw.models.entities.Book;
import ru.otus.hw.models.entities.Genre;

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
