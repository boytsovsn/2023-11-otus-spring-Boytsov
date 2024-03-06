package ru.otus.hw.domain.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Book {
    @Id
    private String id;

    private String title;

//    @DBRef
    private Author author;

//    @DBRef
    private Genre genre;

//    @DBRef
    private List<Remark> remarks;

    public Book(String title, Author author, Genre genre/*, Remark... remarks*/) {
        this.title = title;
        this.author = author;
        this.genre = genre;
//        this.remarks = Arrays.asList(remarks);
    }
}
