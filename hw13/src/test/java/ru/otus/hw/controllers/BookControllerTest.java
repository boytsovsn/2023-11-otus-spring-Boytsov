package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.entities.Book;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(
            username = "user",
            password = "password",
            roles = {"USER"}
    )
    @Test
    @DisplayName("Список книг")
    public void listBookGet() throws Exception {
        List<Book> books = new ArrayList<>();
        books.add(new BookDto(1L, "Test title1", "1", "2").toDomainObject());
        books.add(new BookDto(2L, "Test title2", "2", "2").toDomainObject());
        books.add(new BookDto(3L, "Test title3", "3", "3").toDomainObject());
        given(bookService.findAll()).willReturn(books);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/")
                        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).contains("1", "2", "3");
    }

    @WithMockUser(
            username = "admin",
            password = "password",
            roles = {"ADMIN"}
    )
    @Test
    @DisplayName("Удаление книги")
    public void deleteBookDelete() throws Exception {
        mockMvc.perform(delete("/book/1").with(csrf().asHeader()))
                .andExpect(status().isMovedTemporarily()).andDo(print());
    }

    @WithMockUser(
            username = "admin",
            password = "password",
            roles = {"ADMIN"}
    )
    @Test
    @DisplayName("Редактирование книги")
    public void editBookPut() throws Exception {
        BookDto bookDto = new BookDto(1L, "Test title", "2", "1");
        mockMvc.perform(put("/book/1")
                .param("id", bookDto.getId().toString())
                .param("title", bookDto.getTitle())
                .param("authorId", bookDto.getAuthorId())
                .param("genreId", bookDto.getGenreId()).with(csrf().asHeader()))
            .andExpect(status().isOk()).andDo(print());
    }

    @WithMockUser(
            username = "admin",
            password = "password",
            roles = {"ADMIN"}
    )
    @Test
    @DisplayName("Создание книги")
    public void createBookPost() throws Exception {
        BookDto bookDto = new BookDto(0L, "Test title", "2", "3");
        mockMvc.perform(MockMvcRequestBuilders.post("/book")
                        .param("title", bookDto.getTitle())
                        .param("authorId", bookDto.getAuthorId())
                        .param("genreId", bookDto.getGenreId())
                        .with(csrf().asHeader()))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("Список книг без аутентификации")
    public void listBookGetUnauthorized() throws Exception {
        List<Book> books = new ArrayList<>();
        books.add(new BookDto(1L, "Test title1", "1", "2").toDomainObject());
        books.add(new BookDto(2L, "Test title2", "2", "2").toDomainObject());
        books.add(new BookDto(3L, "Test title3", "3", "3").toDomainObject());
        given(bookService.findAll()).willReturn(books);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/")
                )
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("Удаление книги без аутентификации")
    public void deleteBookDeleteUnauthorized() throws Exception {
        mockMvc.perform(delete("/book/1").with(csrf().asHeader()))
                .andExpect(status().isUnauthorized()).andDo(print());
    }

    @Test
    @DisplayName("Редактирование книги без аутентификации")
    public void editBookPutUnauthorized() throws Exception {
        BookDto bookDto = new BookDto(1L, "Test title", "2", "3");
        mockMvc.perform(put("/book/1")
                        .param("id", bookDto.getId().toString())
                        .param("title", bookDto.getTitle())
                        .param("authorId", bookDto.getAuthorId())
                        .param("genreId", bookDto.getGenreId()).with(csrf().asHeader()))
                .andExpect(status().isUnauthorized()).andDo(print());
    }


    @Test
    @DisplayName("Создание книги без аутентификации")
    public void createBookPostUnauthorized() throws Exception {
        BookDto bookDto = new BookDto(0L, "Test title", "1", "2");
        mockMvc.perform(MockMvcRequestBuilders.post("/book")
                        .param("title", bookDto.getTitle())
                        .param("authorId", bookDto.getAuthorId())
                        .param("genreId", bookDto.getGenreId())
                        .with(csrf().asHeader()))
                .andExpect(status().isUnauthorized()).andDo(print());
    }
}