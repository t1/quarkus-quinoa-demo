package com.example;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Entity
@Schema(description = "A book in the catalog")
public class Book extends PanacheEntity {

    @Schema(description = "Book title", examples = "The Hobbit", required = true)
    public String title;

    @Schema(description = "Author name", examples = "J.R.R. Tolkien", required = true)
    public String author;

    @Schema(description = "ISBN-13 number", examples = "978-0547928227")
    public String isbn;

    @Schema(description = "Year the book was published", examples = "1937")
    public Integer publicationYear;
}
