package ru.otus.hw.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Remark {
    @Id
    private String id;

    private String remarkText;

    @DBRef
    private Book book;

    public Remark(String text, Book book) {
        this.remarkText = text;
        this.setBook(book);
    }

    @Override
    public String toString() {
        return "Remark id = %s, remarkText = %s, book id = %s".formatted(id, remarkText, book.getId());
    }

    @Override
    public boolean equals(Object remarkObject) {
        if (remarkObject == null)
            return false;
        if (!(remarkObject instanceof Remark)) {
            return false;
        }
        Remark remarkTo = (Remark) remarkObject;
        if (id != null && id.equals(remarkTo.getId()) &&
                (remarkText != null && remarkText.equals(remarkTo.getRemarkText())) &&
                (book != null && book.getId() != null && remarkTo.getBook() != null &&
                        book.getId().equals(remarkTo.getBook().getId()))) {
            return true;
        } else
            return false;
    }

    @Override
    public int hashCode() {
        int hash = 37;
        hash = hash + id.hashCode();
        hash = hash*17 + remarkText.hashCode();
        hash = hash*17 + book.getId().hashCode();
        return hash;
    }
}
