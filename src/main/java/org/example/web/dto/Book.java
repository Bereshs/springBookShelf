package org.example.web.dto;

import javax.validation.constraints.*;

public class Book {
    private Integer id;
    @NotEmpty(message = "author is empty")
    private String author;
    @NotEmpty(message = "title is empty")
    private String title;

    @NotNull(message = "size too small")
    @Max(value = 1000, message = "size too large")
    private Integer size;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", size=" + size +
                '}';
    }
}
