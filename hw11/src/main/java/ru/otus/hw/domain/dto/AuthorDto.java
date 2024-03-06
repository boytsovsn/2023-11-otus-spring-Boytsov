package ru.otus.hw.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.domain.entities.Author;

@Data
@AllArgsConstructor
public class AuthorDto {

    private String id;

    private String fullName;

    public Author toDomainObject(){
        return new Author(id, fullName);
    }

    public static AuthorDto fromDomainObject(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }
}
