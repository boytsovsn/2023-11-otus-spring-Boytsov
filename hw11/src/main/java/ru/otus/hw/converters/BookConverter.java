package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.domain.entities.Book;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    private final RemarkConverter remarkConverter;

    public String bookToString(Book book) {
        return "Id: %d, title: %s, author: {%s}, genres: [%s], remarks: [%s]".formatted(
                book.getId(),
                book.getTitle(),
                book.getAuthorId(),
                book.getGenreId(),
                remarkConverter.remarksToString(book.getRemarks()));
    }
}
