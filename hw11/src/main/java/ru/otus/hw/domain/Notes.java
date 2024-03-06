package ru.otus.hw.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Notes {

    @Id
    private String id;

    private String noteText;


    private String personId;

    @Override
    public String toString() {
        return "Notes{" +
                "id=" + id +
                ", noteText='" + noteText + '\'' +
                ", personId=" + personId +
                '}';
    }
}
