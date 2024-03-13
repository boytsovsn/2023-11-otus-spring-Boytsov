package ru.otus.hw;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.hw.domain.entities.Author;
import ru.otus.hw.domain.entities.Book;
import ru.otus.hw.domain.entities.Genre;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.GenreRepository;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class DataFiller implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(DataFiller.class);

//    private final PersonRepository personRepository;
//    private final NotesRepository notesRepository;
//    private final PersonRepositoryCustom personRepositoryCustom;
    private final Scheduler workerPool;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final BookService bookService;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    public void run(ApplicationArguments args) {
//        personRepository.saveAll(Arrays.asList(
//                new Person("Pushkin", 22),
//                new Person("Lermontov", 22),
//                new Person("Tolstoy", 60)
//        )).publishOn(workerPool)
//                .subscribe(savedPerson -> {
//                    logger.info("saved person:{}", savedPerson);
//                    notesRepository.saveAll(Arrays.asList(
//                                    new Notes(null, "txt_1_" + savedPerson.getId(), savedPerson.getId()),
//                                    new Notes(null, "txt_2_" + savedPerson.getId(), savedPerson.getId())))
//                            .publishOn(workerPool)
//                            .subscribe(savedNotes -> logger.info("saved notes:{}", savedNotes));
//                });

//        personRepositoryCustom.findAll()
//                .publishOn(workerPool)
//                .subscribe(personDto -> logger.info("personDto:{}", personDto));
        bookRepository.deleteAll().publishOn(workerPool).subscribe();
        authorRepository.deleteAll().publishOn(workerPool).subscribe();
        genreRepository.deleteAll().publishOn(workerPool).subscribe();
        List<Book> books = bookService.saveAll(Arrays.asList(
                new Book("Voina i mir", null, null),
                new Book("Borodino", null, null),
                new Book("Zolotaya rybka", null, null)))
            .collectList().block();
        List<Author> authors = authorService.saveAll(Arrays.asList(
                        new Author("Pushkin"),
                        new Author("Lermontov"),
                        new Author("Tolstoy")))
            .collectList().block();
        List<Genre> genres = genreService.saveAll(Arrays.asList(
                        new Genre("Roman"),
                        new Genre("Poema"),
                        new Genre("Skazka")))
            .collectList().block();

        int i=0;
        for (Book book: books) {
            bookService.update(book.getId(), book.getTitle(), authors.get(i).getId(), genres.get(i++).getId())
                            .block();
        }

    }
}
