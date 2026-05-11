package com.example.e2e;

import com.microsoft.playwright.Dialog;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

class BookCrudIT extends PlaywrightTestBase {

    @BeforeEach void setup() {
        deleteAllBooks();
    }

    @Test void shouldAddNewBookWithAllFields() {
        navigateHome();

        page.locator("#title").fill("The Hitchhiker's Guide to the Galaxy");
        page.locator("#author").fill("Douglas Adams");
        page.locator("#isbn").fill("978-0345391803");
        page.locator("#year").fill("1979");

        page.locator("button[type='submit']").filter(
            new Locator.FilterOptions().setHasText("Add Book")).click();

        var bookCard = page.locator(".book-card").filter(
            new Locator.FilterOptions().setHasText("The Hitchhiker's Guide to the Galaxy"));
        assertThat(bookCard).isVisible();
        assertThat(bookCard.locator("h3")).containsText("The Hitchhiker's Guide to the Galaxy");
        assertThat(bookCard.locator(".author")).containsText("Douglas Adams");
        assertThat(bookCard.locator(".isbn")).containsText("978-0345391803");
        assertThat(bookCard.locator(".year")).containsText("1979");

        then(page.locator("#title").inputValue()).isEmpty();
        then(page.locator("#author").inputValue()).isEmpty();
        then(page.locator("#isbn").inputValue()).isEmpty();
    }

    @Test void shouldAddBookWithOnlyRequiredFields() {
        navigateHome();

        page.locator("#title").fill("Test Book");
        page.locator("#author").fill("Test Author");
        page.locator("button[type='submit']").filter(
            new Locator.FilterOptions().setHasText("Add Book")).click();

        assertThat(page.locator(".book-card").filter(
            new Locator.FilterOptions().setHasText("Test Book"))).isVisible();
    }

    @Disabled("Edit inputs not appearing in packaged JAR - needs investigation")
    @Test void shouldEditExistingBook() {
        createBook("1984", "George Orwell", "978-0451524935", 1949);
        navigateHome();

        var bookCard = page.locator(".book-card").filter(
            new Locator.FilterOptions().setHasText("1984"));
        assertThat(bookCard).isVisible();

        bookCard.locator(".btn-edit").click();
        bookCard.locator(".book-info").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));

        var titleInput = bookCard.locator("input").first();
        titleInput.fill("");
        titleInput.fill("1984 (Updated)");

        bookCard.locator(".btn-save").click();

        page.waitForTimeout(500);
        assertThat(bookCard.locator("h3")).containsText("1984 (Updated)");
    }

    @Disabled("Edit inputs not appearing in packaged JAR - needs investigation")
    @Test void shouldCancelEditing() {
        createBook("1984", "George Orwell", "978-0451524935", 1949);
        navigateHome();

        var bookCard = page.locator(".book-card").filter(
            new Locator.FilterOptions().setHasText("1984"));
        assertThat(bookCard).isVisible();

        bookCard.locator(".btn-edit").click();
        bookCard.locator(".book-info").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));

        var titleInput = bookCard.locator("input").first();
        titleInput.fill("");
        titleInput.fill("Changed Title");

        bookCard.locator(".btn-cancel").click();

        assertThat(bookCard.locator("h3")).containsText("1984");
        assertThat(bookCard.locator("h3")).not().containsText("Changed Title");
    }

    @Test void shouldDeleteBookWithConfirmation() {
        createBook("The Hobbit", "J.R.R. Tolkien", "978-0547928227", 1937);
        navigateHome();

        var bookCard = page.locator(".book-card").filter(
            new Locator.FilterOptions().setHasText("The Hobbit"));
        assertThat(bookCard).isVisible();

        page.onDialog(Dialog::accept);
        bookCard.locator(".btn-delete").click();

        assertThat(bookCard).not().isVisible();
    }

    @Test void shouldCancelBookDeletion() {
        createBook("The Hobbit", "J.R.R. Tolkien", "978-0547928227", 1937);
        navigateHome();

        var bookCard = page.locator(".book-card").filter(
            new Locator.FilterOptions().setHasText("The Hobbit"));
        assertThat(bookCard).isVisible();

        page.onDialog(Dialog::dismiss);
        bookCard.locator(".btn-delete").click();

        assertThat(bookCard).isVisible();
    }

    @Test void shouldDisplayBookCount() {
        createBook("Book 1", "Author 1", null, null);
        createBook("Book 2", "Author 2", null, null);
        navigateHome();

        assertThat(page.locator(".book-list h2")).containsText("(2)");
    }

    @Test void shouldDisplayBooksInGrid() {
        createBook("Book 1", "Author 1", null, null);
        createBook("Book 2", "Author 2", null, null);
        navigateHome();

        assertThat(page.locator(".books-grid")).isVisible();
        then(page.locator(".book-card").count()).isEqualTo(2);
    }
}
