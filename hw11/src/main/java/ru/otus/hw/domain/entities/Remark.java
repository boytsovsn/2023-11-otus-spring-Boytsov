package ru.otus.hw.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Remark {
    @Id
    private String id;

    private String remarkText;

    private String bookId;

    public Remark(String text, String bookId) {
        this.setRemarkText(text);
        this.setBookId(bookId);
    }

    @Override
    public String toString() {
        return "Remark id = %s, remarkText = %s, book id = %s".formatted(id, remarkText, bookId);
    }
}
