package ru.otus.hw.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="remarks")
public class Remark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String remarkText;

    @ManyToOne
    private Book book;

    @Override
    public String toString() {
        return "Remark id = %d, remarkText = %s, book id = %d".formatted(id, remarkText, book.getId());
    }
}
