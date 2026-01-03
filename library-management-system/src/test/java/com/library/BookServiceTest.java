package com.library;

import com.library.dto.BookDTO;
import com.library.model.Book;
import com.library.repository.BookRepository;
import com.library.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void testGetAllBooks() {
        // Arrange
        Book book1 = Book.builder()
                .id(1L)
                .title("1984")
                .isbn("9780451524935")
                .quantity(5)
                .availableCopies(3)
                .build();

        Book book2 = Book.builder()
                .id(2L)
                .title("Animal Farm")
                .isbn("9780451526342")
                .quantity(4)
                .availableCopies(4)
                .build();

        List<Book> books = Arrays.asList(book1, book2);
        Page<Book> bookPage = new PageImpl<>(books, PageRequest.of(0, 10), books.size());

        when(bookRepository.findAll(any(PageRequest.class))).thenReturn(bookPage);

        // Act
        Page<BookDTO> result = bookService.getAllBooks(0, 10, "title", "ASC");

        // Assert
        assertEquals(2, result.getTotalElements());
        assertEquals("1984", result.getContent().get(0).getTitle());
        assertEquals("Animal Farm", result.getContent().get(1).getTitle());
    }

    @Test
    void testGetBookById_Success() {
        // Arrange
        Book book = Book.builder()
                .id(1L)
                .title("1984")
                .isbn("9780451524935")
                .quantity(5)
                .availableCopies(3)
                .build();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Act
        BookDTO result = bookService.getBookById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("1984", result.getTitle());
        assertEquals("9780451524935", result.getIsbn());
    }

    @Test
    void testGetBookById_NotFound() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> bookService.getBookById(1L));
    }

    @Test
    void testCreateBook() {
        // Arrange
        BookDTO bookDTO = BookDTO.builder()
                .title("New Book")
                .isbn("1234567890")
                .quantity(5)
                .build();

        Book book = Book.builder()
                .id(1L)
                .title("New Book")
                .isbn("1234567890")
                .quantity(5)
                .availableCopies(5)
                .build();

        when(bookRepository.findByIsbn("1234567890")).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // Act
        BookDTO result = bookService.createBook(bookDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New Book", result.getTitle());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testDeleteBook() {
        // Arrange
        when(bookRepository.existsById(1L)).thenReturn(true);

        // Act
        bookService.deleteBook(1L);

        // Assert
        verify(bookRepository, times(1)).deleteById(1L);
    }
}
