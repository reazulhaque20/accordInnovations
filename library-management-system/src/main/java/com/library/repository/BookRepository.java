package com.library.repository;

import com.library.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Book> findByAuthor_LastNameContainingIgnoreCase(String authorName, Pageable pageable);

    Page<Book> findByGenreContainingIgnoreCase(String genre, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE " +
            "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:authorName IS NULL OR LOWER(b.author.lastName) LIKE LOWER(CONCAT('%', :authorName, '%'))) AND " +
            "(:genre IS NULL OR LOWER(b.genre) LIKE LOWER(CONCAT('%', :genre, '%'))) AND " +
            "(:isbn IS NULL OR b.isbn = :isbn) AND " +
            "(:publicationYear IS NULL OR b.publicationYear = :publicationYear)")
    Page<Book> searchBooks(@Param("title") String title,
                           @Param("authorName") String authorName,
                           @Param("genre") String genre,
                           @Param("isbn") String isbn,
                           @Param("publicationYear") Integer publicationYear,
                           Pageable pageable);

    // JOIN Query Example
    @Query("SELECT b.title, a.firstName, a.lastName, COUNT(l) as loanCount " +
            "FROM Book b " +
            "JOIN b.author a " +
            "LEFT JOIN Loan l ON l.book.id = b.id AND l.returnDate IS NULL " +
            "GROUP BY b.id, a.id")
    List<Object[]> findBooksWithAuthorAndActiveLoans();
}
