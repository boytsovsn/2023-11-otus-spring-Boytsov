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
public class Person {

    @Id
    private String id;

    @NotNull
    private String lastName;

    private int age;


    public Person(String lastName, int age) {
        this(null, lastName,  age);
    }


    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                '}';
    }
}
