package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.models.entities.Author;
import ru.otus.hw.models.entities.Book;
import ru.otus.hw.models.entities.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {
    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void getBookList() throws Exception {
        List<Book> books = new ArrayList<>();
        books.add(new BookDto("0515e04eeead46478298faa1", "Test title1", "65ce2a68d4437132eb18a431", "65ce2a68d4437132eb18a43e", null, null).toDomainObject());
        books.add(new BookDto("0515e04eeead46478298faa2", "Test title2", "65ce2a68d4437132eb18a432", "65ce2a68d4437132eb18a43e", null, null).toDomainObject());
        books.add(new BookDto("0515e04eeead46478298faa3", "Test title3", "65ce2a68d4437132eb18a433", "65ce2a68d4437132eb18a43f", null, null).toDomainObject());
        List<Author> authors = Arrays.asList(new Author("65ce2a68d4437132eb18a431", "Lermontov"), new Author("65ce2a68d4437132eb18a432", "Pushkin"));
        given(authorService.findAll()).willReturn(authors);
        List<Genre> genres = Arrays.asList(new Genre("65ce2a68d4437132eb18a43e", "Skazka"), new Genre("65ce2a68d4437132eb18a43f", "Poema"));
        given(genreService.findAll()).willReturn(genres);
        given(bookService.findAll()).willReturn(books);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/book"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).contains("0515e04eeead46478298faa1", "0515e04eeead46478298faa2", "0515e04eeead46478298faa3", "Lermontov", "Pushkin", "Skazka", "Poema");
    }

    @Test
    public void getBook() throws Exception {
        Book book = new BookDto("0515e04eeead46478298faa1", "Test title1", "65ce2a68d4437132eb18a431", "65ce2a68d4437132eb18a43e", null, null).toDomainObject();
        List<Author> authors = Arrays.asList(new Author("65ce2a68d4437132eb18a431", "Lermontov"), new Author("65ce2a68d4437132eb18a432", "Pushkin"));
        given(authorService.findAll()).willReturn(authors);
        List<Genre> genres = Arrays.asList(new Genre("65ce2a68d4437132eb18a43e", "Skazka"), new Genre("65ce2a68d4437132eb18a43f", "Poema"));
        given(genreService.findAll()).willReturn(genres);
        given(bookService.findById("0515e04eeead46478298faa1")).willReturn(Optional.of(book));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/book/0515e04eeead46478298faa1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).contains("0515e04eeead46478298faa1", "65ce2a68d4437132eb18a431", "65ce2a68d4437132eb18a43e", "Lermontov", "Skazka");
    }

    @Test
    public void deleteBookDelete() throws Exception {
        mockMvc.perform(delete("/api/book/0515e04eeead46478298faa1"))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void editBookPut() throws Exception {
        BookDto bookDto = new BookDto("0515e04eeead46478298faa1", "Test title", "65ce2a68d4437132eb18a431", "65ce2a68d4437132eb18a43e", null, null);
        Book book = bookDto.toDomainObject();
        given(bookService.update(bookDto.getId(), bookDto.getTitle(), bookDto.getAuthorId(), bookDto.getGenreId())).willReturn(book);
        MvcResult mvcResult = mockMvc.perform(put("/api/book/0515e04eeead46478298faa1")
                .param("id", bookDto.getId())
                .param("title", bookDto.getTitle())
                .param("authorId", bookDto.getAuthorId())
                .param("genreId", bookDto.getGenreId()))
            .andExpect(status().isOk()).andDo(print()).andReturn();
        assertThat(mvcResult.getResponse().getContentAsString()).contains("0515e04eeead46478298faa1", "65ce2a68d4437132eb18a431", "65ce2a68d4437132eb18a43e");
    }

    @Test
    public void createBookPost() throws Exception {
        BookDto bookDto = new BookDto("0", "Test title", "65ce2a68d4437132eb18a431", "65ce2a68d4437132eb18a43e", null, null);
        Book book = bookDto.toDomainObject();
        book.setId("0515e04eeead46478298faa1");
        given(bookService.insert(bookDto.getTitle(), bookDto.getAuthorId(), bookDto.getGenreId())).willReturn(book);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/book")
                    .param("title", bookDto.getTitle())
                    .param("authorId", bookDto.getAuthorId())
                    .param("genreId", bookDto.getGenreId()))
                .andExpect(status().isCreated())
                .andDo(print()).andReturn();
        assertThat(mvcResult.getResponse().getContentAsString()).contains("0515e04eeead46478298faa1", "65ce2a68d4437132eb18a431", "65ce2a68d4437132eb18a43e");
    }

}