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
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.entities.Book;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
import java.util.List;

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
    public void listBookGet() throws Exception {
        List<Book> books = new ArrayList<>();
        books.add(new BookDto("0515e04eeead46478298faa1", "Test title1", "65ce2a68d4437132eb18a431", "65ce2a68d4437132eb18a43e").toDomainObject());
        books.add(new BookDto("0515e04eeead46478298faa2", "Test title2", "65ce2a68d4437132eb18a432", "65ce2a68d4437132eb18a43e").toDomainObject());
        books.add(new BookDto("0515e04eeead46478298faa3", "Test title3", "65ce2a68d4437132eb18a433", "65ce2a68d4437132eb18a43f").toDomainObject());
        given(bookService.findAll()).willReturn(books);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/books")
                        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).contains("0515e04eeead46478298faa1", "0515e04eeead46478298faa2", "0515e04eeead46478298faa3");
    }

    @Test
    public void deleteBookDelete() throws Exception {
        mockMvc.perform(delete("/api/book/0515e04eeead46478298faa1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMovedTemporarily()).andDo(print());
    }

    @Test
    public void editBookPut() throws Exception {
        BookDto bookDto = new BookDto("0515e04eeead46478298faa1", "Test title", "65ce2a68d4437132eb18a431", "65ce2a68d4437132eb18a43e");
        mockMvc.perform(put("/book/0515e04eeead46478298faa1")
                .param("id", bookDto.getId())
                .param("title", bookDto.getTitle())
                .param("authorId", bookDto.getAuthorId())
                .param("genreId", bookDto.getGenreId()))
            .andExpect(status().isMovedTemporarily()).andDo(print());
    }

    @Test
    public void createBookPost() throws Exception {
        BookDto bookDto = new BookDto("0", "Test title", "65ce2a68d4437132eb18a431", "65ce2a68d4437132eb18a43e");
        mockMvc.perform(MockMvcRequestBuilders.post("/book").
                        param("title", bookDto.getTitle()).param("authorId", bookDto.getAuthorId()).param("genreId", bookDto.getGenreId()))
                .andExpect(status().isMovedTemporarily()).andDo(print());
    }

}