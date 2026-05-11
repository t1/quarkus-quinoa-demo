package com.example;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

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
    public Response getById(@Parameter(description = "Book ID", required = true) @PathParam("id") Long id) {
        Book book = Book.findById(id);
        if (book == null) {
            return Response.status(NOT_FOUND).build();
        }
        return Response.ok(book).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Create a new book", description = "Adds a new book to the catalog")
    @APIResponse(responseCode = "201", description = "Book created",
            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Book.class)))
    @APIResponse(responseCode = "400", description = "Invalid input")
    public Response create(Book book) {
        book.persist();
        return Response.status(Response.Status.CREATED).entity(book).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Update a book", description = "Updates an existing book's information")
    @APIResponse(responseCode = "200", description = "Book updated",
            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Book.class)))
    @APIResponse(responseCode = "404", description = "Book not found")
    @APIResponse(responseCode = "400", description = "Invalid input")
    public Response update(@Parameter(description = "Book ID", required = true) @PathParam("id") Long id, Book updatedBook) {
        Book book = Book.findById(id);
        if (book == null) {
            return Response.status(NOT_FOUND).build();
        }
        book.title = updatedBook.title;
        book.author = updatedBook.author;
        book.isbn = updatedBook.isbn;
        book.publicationYear = updatedBook.publicationYear;
        return Response.ok(book).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Delete a book", description = "Removes a book from the catalog")
    @APIResponse(responseCode = "204", description = "Book deleted")
    @APIResponse(responseCode = "404", description = "Book not found")
    public Response delete(@Parameter(description = "Book ID", required = true) @PathParam("id") Long id) {
        boolean deleted = Book.deleteById(id);
        if (!deleted) {
            return Response.status(NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
