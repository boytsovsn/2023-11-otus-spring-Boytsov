package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.entities.Author;
import ru.otus.hw.models.entities.Genre;

@Data
@AllArgsConstructor
public class GenreDto {

    private String id;

    private String name;

    public Genre toDomainObject(){
        return new Genre(id, name);
    }

    public static GenreDto fromDomainObject(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}
