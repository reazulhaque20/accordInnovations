package com.library.dto;

import lombok.Data;

@Data
public class BookSearchRequest {
    private String title;
    private String authorName;
    private String genre;
    private String isbn;
    private Integer publicationYear;
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "title";
    private String sortDirection = "ASC";
}
