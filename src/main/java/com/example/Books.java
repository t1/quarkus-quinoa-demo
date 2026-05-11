package com.example;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.validation.Valid;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/api/books")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Tag(name = "Books", description = "Book catalog operations")
public class Books {

    @GET
    @Operation(summary = "List all books", description = "Returns all books in the catalog")
    @APIResponse(responseCode = "200", description = "Success",
            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Book.class)))
    public List<Book> listAll() {
        return Book.listAll();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get book by ID", description = "Returns a single book by its unique identifier")
    @APIResponse(responseCode = "200", description = "Book found",
            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Book.class)))
    @APIResponse(responseCode = "404", description = "Book not found")
    public Book getById(@Parameter(description = "Book ID", required = true) @PathParam("id") Long id) {
        return Book.<Book>findByIdOptional(id)
                .orElseThrow(NotFoundException::new);
    }

    @POST
    @Transactional
    @ResponseStatus(201)
    @Operation(summary = "Create a new book", description = "Adds a new book to the catalog")
    @APIResponse(responseCode = "201", description = "Book created",
            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Book.class)))
    @APIResponse(responseCode = "400", description = "Invalid input")
    public Book create(@Valid Book book) {
        book.persist();
        return book;
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Update a book", description = "Updates an existing book's information")
    @APIResponse(responseCode = "200", description = "Book updated",
            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Book.class)))
    @APIResponse(responseCode = "404", description = "Book not found")
    @APIResponse(responseCode = "400", description = "Invalid input")
    public Book update(@Parameter(description = "Book ID", required = true) @PathParam("id") Long id, @Valid Book updatedBook) {
        var book = Book.<Book>findByIdOptional(id)
                .orElseThrow(NotFoundException::new);
        book.updateFrom(updatedBook);
        return book;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Delete a book", description = "Removes a book from the catalog")
    @APIResponse(responseCode = "200", description = "Book deleted",
            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Book.class)))
    @APIResponse(responseCode = "404", description = "Book not found")
    public Book delete(@Parameter(description = "Book ID", required = true) @PathParam("id") Long id) {
        var book = Book.<Book>findByIdOptional(id)
                .orElseThrow(NotFoundException::new);
        book.delete();
        return book;
    }
}
