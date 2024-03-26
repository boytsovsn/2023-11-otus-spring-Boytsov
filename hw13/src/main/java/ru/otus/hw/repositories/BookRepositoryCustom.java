package ru.otus.hw.repositories;

import ru.otus.hw.models.entities.Book;

import java.util.List;

public interface BookRepositoryCustom {

    List<Book> findAll();

}
