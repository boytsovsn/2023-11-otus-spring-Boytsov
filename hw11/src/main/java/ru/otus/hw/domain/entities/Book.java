package ru.otus.hw.domain.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Book {
    @Id
    private String id;

    private String title;

    private String authorId;

    private String genreId;

    private List<Remark> remarks;

    public Book(String title, Author author, Genre genre/*, Remark... remarks*/) {
        this.title = title;
        this.authorId = author != null ? author.getId() : null;
        this.genreId = genre != null ? genre.getId() : null;
//        this.remarks = Arrays.asList(remarks);
    }
}
