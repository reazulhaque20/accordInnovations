package com.library.controller;

import com.library.dto.ApiResponse;
import com.library.dto.BookDTO;
import com.library.dto.BookSearchRequest;
import com.library.service.BookService;
import com.library.service.ExternalApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name = "Book Management", description = "APIs for managing library books")
public class BookController {

    private final BookService bookService;
    private final ExternalApiService externalApiService;

    @GetMapping
    @Operation(summary = "Get all books with pagination")
    public ResponseEntity<ApiResponse<Page<BookDTO>>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {

        Page<BookDTO> books = bookService.getAllBooks(page, size, sortBy, sortDir);

        ApiResponse<Page<BookDTO>> response = ApiResponse.<Page<BookDTO>>builder()
                .success(true)
                .message("Books retrieved successfully")
                .data(books)
                .statusCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID")
    public ResponseEntity<ApiResponse<BookDTO>> getBookById(@PathVariable Long id) {
        BookDTO book = bookService.getBookById(id);

        ApiResponse<BookDTO> response = ApiResponse.<BookDTO>builder()
                .success(true)
                .message("Book retrieved successfully")
                .data(book)
                .statusCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create a new book")
    public ResponseEntity<ApiResponse<BookDTO>> createBook(@Valid @RequestBody BookDTO bookDTO) {
        BookDTO createdBook = bookService.createBook(bookDTO);

        ApiResponse<BookDTO> response = ApiResponse.<BookDTO>builder()
                .success(true)
                .message("Book created successfully")
                .data(createdBook)
                .statusCode(HttpStatus.CREATED.value())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing book")
    public ResponseEntity<ApiResponse<BookDTO>> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookDTO bookDTO) {

        BookDTO updatedBook = bookService.updateBook(id, bookDTO);

        ApiResponse<BookDTO> response = ApiResponse.<BookDTO>builder()
                .success(true)
                .message("Book updated successfully")
                .data(updatedBook)
                .statusCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Book deleted successfully")
                .statusCode(HttpStatus.NO_CONTENT.value())
                .build();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @PostMapping("/search")
    @Operation(summary = "Search books with pagination and filters")
    public ResponseEntity<ApiResponse<Page<BookDTO>>> searchBooks(
            @RequestBody BookSearchRequest searchRequest) {

        Page<BookDTO> books = bookService.searchBooks(searchRequest);

        ApiResponse<Page<BookDTO>> response = ApiResponse.<Page<BookDTO>>builder()
                .success(true)
                .message("Books search completed")
                .data(books)
                .statusCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/external/search")
    @Operation(summary = "Search books from Google Books API")
    public ResponseEntity<ApiResponse<Map<String, Object>>> searchExternalBooks(
            @RequestParam String query) {

        Map<String, Object> externalBooks = externalApiService.searchGoogleBooks(query);

        ApiResponse<Map<String, Object>> response = ApiResponse.<Map<String, Object>>builder()
                .success(true)
                .message("External books search completed")
                .data(externalBooks)
                .statusCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/external/call")
    @Operation(summary = "Call external API (demo)")
    public ResponseEntity<ApiResponse<String>> callExternalApi(
            @RequestParam(defaultValue = "https://jsonplaceholder.typicode.com/posts/1") String url) {

        String externalResponse = externalApiService.callExternalApi(url);

        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("External API called successfully")
                .data(externalResponse)
                .statusCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/loans/active")
    @Operation(summary = "Get books with active loans (JOIN query example)")
    public ResponseEntity<ApiResponse<List<Object[]>>> getBooksWithActiveLoans() {
        List<Object[]> booksWithLoans = bookService.getBooksWithActiveLoans();

        ApiResponse<List<Object[]>> response = ApiResponse.<List<Object[]>>builder()
                .success(true)
                .message("Books with active loans retrieved")
                .data(booksWithLoans)
                .statusCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.ok(response);
    }
}
