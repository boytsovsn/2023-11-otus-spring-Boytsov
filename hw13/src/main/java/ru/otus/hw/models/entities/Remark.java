package ru.otus.hw.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Remark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String remarkText;

    @ManyToOne
    private Book book;

    public Remark(String text, Book book) {
        this.remarkText = text;
        this.setBook(book);
    }

    @Override
    public String toString() {
        return "Remark id = %d, remarkText = %s, book id = %d".formatted(id, remarkText, book.getId());
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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
