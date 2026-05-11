package com.example.e2e;

import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.LoadState.NETWORKIDLE;

class UINavigationIT extends PlaywrightTestBase {

    @BeforeEach void setup() {
        deleteAllBooks();
    }

    @Test void shouldLoadHomePage() {
        navigateHome();

        assertThat(page.locator("h1")).containsText("📚 Book Catalog");
        assertThat(page.locator(".subtitle")).containsText("Manage your book collection");
    }

    @Test void shouldDisplayAddBookForm() {
        navigateHome();

        assertThat(page.locator("h2").filter(new Locator.FilterOptions()
            .setHasText("Add New Book"))).isVisible();
        assertThat(page.locator("#title")).isVisible();
        assertThat(page.locator("#author")).isVisible();
        assertThat(page.locator("#isbn")).isVisible();
        assertThat(page.locator("#year")).isVisible();
        assertThat(page.locator("button[type='submit']").filter(
            new Locator.FilterOptions().setHasText("Add Book"))).isVisible();
    }

    @Test void shouldDisplayBookList() {
        navigateHome();

        page.waitForLoadState(NETWORKIDLE);

        assertThat(page.locator(".book-list")).isVisible();
        assertThat(page.locator(".book-list h2")).isVisible();
    }

    @Test void shouldHaveResponsiveDesign() {
        page.setViewportSize(1200, 800);
        navigateHome();
        assertThat(page.locator(".container")).isVisible();

        page.setViewportSize(375, 667);
        assertThat(page.locator(".container")).isVisible();
        assertThat(page.locator("h1")).isVisible();
    }

    @Test void shouldHaveCorrectPageStructure() {
        navigateHome();

        assertThat(page.locator("header")).isVisible();
        assertThat(page.locator("main")).isVisible();
        assertThat(page.locator(".book-form")).isVisible();
        assertThat(page.locator(".book-list")).isVisible();
    }
}
