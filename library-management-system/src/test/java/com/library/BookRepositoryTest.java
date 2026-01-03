package com.library;

import com.library.model.Book;
import com.library.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void testFindByIsbn() {
        // Save a book
        Book book = Book.builder()
                .title("Test Book")
                .isbn("1234567890")
                .quantity(5)
                .availableCopies(5)
                .build();

        bookRepository.save(book);

        // Test findByIsbn
        Optional<Book> found = bookRepository.findByIsbn("1234567890");

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Test Book");
    }

    @Test
    void testFindByTitleContaining() {
        // Save books
        Book book1 = Book.builder()
                .title("Java Programming")
                .isbn("1111111111")
                .quantity(3)
                .availableCopies(3)
                .build();

        Book book2 = Book.builder()
                .title("Advanced Java")
                .isbn("2222222222")
                .quantity(2)
                .availableCopies(2)
                .build();

        bookRepository.save(book1);
        bookRepository.save(book2);

        // Test findByTitleContaining
        Page<Book> result = bookRepository.findByTitleContainingIgnoreCase(
                "java",
                PageRequest.of(0, 10)
        );

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).extracting(Book::getTitle)
                .contains("Java Programming", "Advanced Java");
    }
}
