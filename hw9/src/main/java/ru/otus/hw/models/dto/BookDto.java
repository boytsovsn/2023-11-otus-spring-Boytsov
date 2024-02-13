package ru.otus.hw.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.entities.Book;

@Data
@AllArgsConstructor
public class BookDto {

    private String id;

    @NotBlank(message = "{name-field-should-not-be-blank}")
    @Size(min = 2, max = 50, message = "{name-field-should-has-expected-size}")
    private String title;

    public Book toDomainObject(){
        return new Book(id, title, null, null, null);
    }

    public static BookDto fromDomainObject(Book book) {
        return new BookDto(book.getId(), book.getTitle());
    }
}
