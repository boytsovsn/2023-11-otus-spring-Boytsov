package ru.otus.hw.models.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Book {
    @Id
    private String id;

    private String title;

    private Author author;

    private Genre genre;

    private List<Remark> remarks;

    public Book(String title, Author author, Genre genre, Remark... remarks) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.remarks = Arrays.asList(remarks);
    }
}