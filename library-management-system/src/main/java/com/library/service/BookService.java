package com.library.service;

import com.library.dto.BookDTO;
import com.library.dto.BookSearchRequest;
import com.library.model.Book;
import com.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public Page<BookDTO> getAllBooks(int page, int size, String sortBy, String sortDir) {
        log.info("Fetching all books - page: {}, size: {}", page, size);

        Sort sort = sortDir.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return bookRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public BookDTO getBookById(Long id) {
        log.info("Fetching book by id: {}", id);

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Book not found with id: {}", id);
                    return new RuntimeException("Book not found with id: " + id);
                });

        return convertToDTO(book);
    }

    @Transactional
    public BookDTO createBook(BookDTO bookDTO) {
        log.info("Creating new book: {}", bookDTO.getTitle());

        // Check if ISBN already exists
        if (bookDTO.getIsbn() != null &&
                bookRepository.findByIsbn(bookDTO.getIsbn()).isPresent()) {
            log.error("Book with ISBN {} already exists", bookDTO.getIsbn());
            throw new RuntimeException("Book with ISBN " + bookDTO.getIsbn() + " already exists");
        }

        Book book = Book.builder()
                .title(bookDTO.getTitle())
                .isbn(bookDTO.getIsbn())
                .publicationYear(bookDTO.getPublicationYear())
                .genre(bookDTO.getGenre())
                .quantity(bookDTO.getQuantity())
                .availableCopies(bookDTO.getQuantity())
                .build();

        Book savedBook = bookRepository.save(book);
        log.info("Book created successfully with id: {}", savedBook.getId());

        return convertToDTO(savedBook);
    }

    @Transactional
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        log.info("Updating book with id: {}", id);

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Book not found for update with id: {}", id);
                    return new RuntimeException("Book not found with id: " + id);
                });

        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setIsbn(bookDTO.getIsbn());
        existingBook.setPublicationYear(bookDTO.getPublicationYear());
        existingBook.setGenre(bookDTO.getGenre());
        existingBook.setQuantity(bookDTO.getQuantity());
        existingBook.setAvailableCopies(bookDTO.getAvailableCopies());
        existingBook.setUpdatedAt(LocalDateTime.now());

        Book updatedBook = bookRepository.save(existingBook);
        log.info("Book updated successfully with id: {}", updatedBook.getId());

        return convertToDTO(updatedBook);
    }

    @Transactional
    public void deleteBook(Long id) {
        log.info("Deleting book with id: {}", id);

        if (!bookRepository.existsById(id)) {
            log.error("Book not found for deletion with id: {}", id);
            throw new RuntimeException("Book not found with id: " + id);
        }

        bookRepository.deleteById(id);
        log.info("Book deleted successfully with id: {}", id);
    }

    @Transactional(readOnly = true)
    public Page<BookDTO> searchBooks(BookSearchRequest searchRequest) {
        log.info("Searching books with criteria: {}", searchRequest);

        Sort sort = searchRequest.getSortDirection().equalsIgnoreCase("DESC")
                ? Sort.by(searchRequest.getSortBy()).descending()
                : Sort.by(searchRequest.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(
                searchRequest.getPage(),
                searchRequest.getSize(),
                sort
        );

        Page<Book> books = bookRepository.searchBooks(
                searchRequest.getTitle(),
                searchRequest.getAuthorName(),
                searchRequest.getGenre(),
                searchRequest.getIsbn(),
                searchRequest.getPublicationYear(),
                pageable
        );

        log.info("Found {} books matching search criteria", books.getTotalElements());

        return books.map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public List<Object[]> getBooksWithActiveLoans() {
        log.info("Fetching books with active loans using JOIN query");
        return bookRepository.findBooksWithAuthorAndActiveLoans();
    }

    private BookDTO convertToDTO(Book book) {
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .publicationYear(book.getPublicationYear())
                .authorName(book.getAuthor() != null ?
                        book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName() :
                        null)
                .authorId(book.getAuthor() != null ? book.getAuthor().getId() : null)
                .genre(book.getGenre())
                .quantity(book.getQuantity())
                .availableCopies(book.getAvailableCopies())
                .build();
    }
}
