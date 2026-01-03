package com.library.loader;

import com.library.model.Author;
import com.library.model.Book;
import com.library.repository.AuthorRepository;
import com.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SampleDataLoader implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Loading sample data...");

        if (authorRepository.count() == 0 && bookRepository.count() == 0) {
            // Create Authors
            Author author1 = Author.builder()
                    .firstName("George")
                    .lastName("Orwell")
                    .nationality("British")
                    .birthYear(1903)
                    .build();

            Author author2 = Author.builder()
                    .firstName("J.K.")
                    .lastName("Rowling")
                    .nationality("British")
                    .birthYear(1965)
                    .build();

            Author author3 = Author.builder()
                    .firstName("Harper")
                    .lastName("Lee")
                    .nationality("American")
                    .birthYear(1926)
                    .build();

            Author author4 = Author.builder()
                    .firstName("F. Scott")
                    .lastName("Fitzgerald")
                    .nationality("American")
                    .birthYear(1896)
                    .build();

            List<Author> authors = Arrays.asList(author1, author2, author3, author4);
            authorRepository.saveAll(authors);

            // Create Books
            Book book1 = Book.builder()
                    .title("1984")
                    .isbn("9780451524935")
                    .publicationYear(1949)
                    .author(author1)
                    .genre("Dystopian")
                    .quantity(5)
                    .availableCopies(3)
                    .build();

            Book book2 = Book.builder()
                    .title("Animal Farm")
                    .isbn("9780451526342")
                    .publicationYear(1945)
                    .author(author1)
                    .genre("Political Satire")
                    .quantity(4)
                    .availableCopies(4)
                    .build();

            Book book3 = Book.builder()
                    .title("Harry Potter and the Philosopher's Stone")
                    .isbn("9780747532743")
                    .publicationYear(1997)
                    .author(author2)
                    .genre("Fantasy")
                    .quantity(8)
                    .availableCopies(6)
                    .build();

            Book book4 = Book.builder()
                    .title("To Kill a Mockingbird")
                    .isbn("9780061120084")
                    .publicationYear(1960)
                    .author(author3)
                    .genre("Southern Gothic")
                    .quantity(6)
                    .availableCopies(2)
                    .build();

            Book book5 = Book.builder()
                    .title("The Great Gatsby")
                    .isbn("9780743273565")
                    .publicationYear(1925)
                    .author(author4)
                    .genre("Tragedy")
                    .quantity(7)
                    .availableCopies(5)
                    .build();

            Book book6 = Book.builder()
                    .title("Harry Potter and the Chamber of Secrets")
                    .isbn("9780747538486")
                    .publicationYear(1998)
                    .author(author2)
                    .genre("Fantasy")
                    .quantity(7)
                    .availableCopies(7)
                    .build();

            List<Book> books = Arrays.asList(book1, book2, book3, book4, book5, book6);
            bookRepository.saveAll(books);

            log.info("Sample data loaded successfully. Created {} authors and {} books.",
                    authorRepository.count(), bookRepository.count());
        } else {
            log.info("Data already exists. Skipping sample data loading.");
        }
    }
}