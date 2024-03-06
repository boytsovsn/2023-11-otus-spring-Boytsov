package ru.otus.hw.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.domain.entities.Genre;

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
