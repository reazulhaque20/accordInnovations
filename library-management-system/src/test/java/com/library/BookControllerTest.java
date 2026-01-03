package com.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.dto.BookDTO;
import com.library.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllBooks() throws Exception {
        BookDTO book1 = BookDTO.builder()
                .id(1L)
                .title("1984")
                .isbn("9780451524935")
                .build();

        BookDTO book2 = BookDTO.builder()
                .id(2L)
                .title("Animal Farm")
                .isbn("9780451526342")
                .build();

        List<BookDTO> books = Arrays.asList(book1, book2);
        Page<BookDTO> bookPage = new PageImpl<>(books, PageRequest.of(0, 10), books.size());

        when(bookService.getAllBooks(0, 10, "title", "ASC")).thenReturn(bookPage);

        mockMvc.perform(get("/api/v1/books")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "title")
                        .param("sortDir", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].title").value("1984"))
                .andExpect(jsonPath("$.data.content[1].title").value("Animal Farm"));
    }

    @Test
    void testGetBookById() throws Exception {
        BookDTO book = BookDTO.builder()
                .id(1L)
                .title("1984")
                .isbn("9780451524935")
                .build();

        when(bookService.getBookById(1L)).thenReturn(book);

        mockMvc.perform(get("/api/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("1984"))
                .andExpect(jsonPath("$.data.isbn").value("9780451524935"));
    }

    @Test
    void testCreateBook() throws Exception {
        BookDTO newBook = BookDTO.builder()
                .title("New Book")
                .isbn("1234567890")
                .quantity(5)
                .build();

        BookDTO savedBook = BookDTO.builder()
                .id(1L)
                .title("New Book")
                .isbn("1234567890")
                .quantity(5)
                .build();

        when(bookService.createBook(any(BookDTO.class))).thenReturn(savedBook);

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("New Book"));
    }

    @Test
    void testDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/v1/books/1"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.success").value(true));
    }
}
