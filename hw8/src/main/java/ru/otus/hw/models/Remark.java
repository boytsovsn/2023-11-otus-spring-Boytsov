package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
        this.remarkText = text;
        this.setBookId(bookId);
    }

    @Override
    public String toString() {
        return "Remark id = %s, remarkText = %s, book id = %s".formatted(id, remarkText, bookId);
    }
}
