package ru.otus.hw.repositories;

import ru.otus.hw.models.Book;

import java.util.List;

public interface BookRepositoryCustom {

    List<Book> findAll();

}
