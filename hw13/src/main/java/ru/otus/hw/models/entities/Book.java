package ru.otus.hw.models.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;


import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@NamedEntityGraph(name = "book-entity-graph",
        attributeNodes = {@NamedAttributeNode("author"), @NamedAttributeNode("genre")})
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    private Author author;

    @ManyToOne
    private Genre genre;

    @OneToMany(mappedBy="book", targetEntity = Remark.class, orphanRemoval = true)
    private List<Remark> remarks;

    public Book(String title, Author author, Genre genre, Remark... remarks) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.remarks = Arrays.asList(remarks);
    }

    @Override
    public String toString() {
        return "[Id: %d, title: %s, author: %s, genres: %s]".formatted(
                this.getId(),
                this.getTitle(),
                author.toString(),
                genre.toString());
    }

}
