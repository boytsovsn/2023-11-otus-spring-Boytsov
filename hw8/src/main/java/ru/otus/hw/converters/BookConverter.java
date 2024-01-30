package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    private final RemarkConverter remarkConverter;

    public String bookToString(Book book) {
        return "Id: %s, title: %s, author: {%s}, genres: [%s], remarks: [%s]".formatted(
                book.getId(),
                book.getTitle(),
                authorConverter.authorToString(book.getAuthor()),
                genreConverter.genreToString(book.getGenre()),
                remarkConverter.remarksToString(book.getRemarks()));
    }
}
