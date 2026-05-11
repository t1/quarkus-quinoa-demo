package com.example;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Entity
@Schema(description = "A book in the catalog")
public class Book extends PanacheEntity {

    @NotBlank
    @Schema(description = "Book title", examples = "The Hobbit", required = true)
    public String title;

    @NotBlank
    @Schema(description = "Author name", examples = "J.R.R. Tolkien", required = true)
    public String author;

    @Schema(description = "ISBN-13 number", examples = "978-0547928227")
    public String isbn;

    @Schema(description = "Year the book was published", examples = "1937")
    public Integer publicationYear;

    public void updateFrom(Book other) {
        title = other.title;
        author = other.author;
        isbn = other.isbn;
        publicationYear = other.publicationYear;
    }
}
