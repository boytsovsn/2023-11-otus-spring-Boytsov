package ru.otus.hw.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.entities.Author;
import ru.otus.hw.models.entities.Book;
import ru.otus.hw.models.entities.Genre;

@Data
@AllArgsConstructor
public class BookDto {

    private String id;

    @NotBlank(message = "{field-should-not-be-blank}")
    @Size(min = 2, max = 50, message = "{string-field-should-has-expected-size}")
    private String title;

    @NotBlank(message = "{field-should-not-be-blank}")
    @Size(min = 24, max = 24, message = "{id-field-should-has-expected-size}")
    private String authorId;

    @NotBlank(message = "{field-should-not-be-blank}")
    @Size(min = 24, max = 24, message = "{id-field-should-has-expected-size}")
    private String genreId;

    public Book toDomainObject(){
        return new Book(id, title, new Author(authorId, null), new Genre(genreId, null), null);
    }

    public static BookDto fromDomainObject(Book book) {
        return new BookDto(book.getId(), book.getTitle(), book.getAuthor().getId(), book.getGenre().getId());
    }
}
