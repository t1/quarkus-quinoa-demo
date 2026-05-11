package com.example.e2e;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.LoadState.NETWORKIDLE;

/**
 * UI Navigation and Layout Tests
 */
class UINavigationIT extends PlaywrightTestBase {

    @BeforeEach void setup() {
        deleteAllBooks();
    }

    @Test void shouldLoadHomePage() {
        page.onConsoleMessage(msg -> System.out.println("Console [" + msg.type() + "]: " + msg.text()));

        navigateHome();

        // Test if JS assets are accessible
        var jsResponse = page.request().get(baseUrl() + "assets/index-BOpchgwU.js");
        System.out.println("JS asset status: " + jsResponse.status());

        var cssResponse = page.request().get(baseUrl() + "assets/index-Ebahi6lG.css");
        System.out.println("CSS asset status: " + cssResponse.status());

        assertThat(page.locator("h1")).containsText("📚 Book Catalog");
        assertThat(page.locator(".subtitle")).containsText("Manage your book collection");
    }

    @Test void shouldDisplayAddBookForm() {
        navigateHome();

        assertThat(page.locator("h2").filter(new com.microsoft.playwright.Locator.FilterOptions()
            .setHasText("Add New Book"))).isVisible();
        assertThat(page.locator("#title")).isVisible();
        assertThat(page.locator("#author")).isVisible();
        assertThat(page.locator("#isbn")).isVisible();
        assertThat(page.locator("#year")).isVisible();
        assertThat(page.locator("button[type='submit']").filter(
            new com.microsoft.playwright.Locator.FilterOptions().setHasText("Add Book"))).isVisible();
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
