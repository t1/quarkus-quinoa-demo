package com.example.e2e;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.net.URL;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Base class for Playwright E2E tests.
 * Uses Quarkus integration test to ensure the app is running.
 */
@QuarkusIntegrationTest
public abstract class PlaywrightTestBase {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @TestHTTPResource("/") URL baseUrl;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    @AfterAll
    static void closeBrowser() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        if (context != null) context.close();
    }

    String baseUrl() {
        return baseUrl.toString();
    }

    void navigateHome() {
        page.navigate(baseUrl());
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        // Give Vue time to hydrate
        page.waitForTimeout(1000);
    }

    void deleteAllBooks() {
        var request = page.request();
        var response = request.get(baseUrl() + "api/books");

        if (!response.ok()) return;

        var text = response.text();
        if (text.equals("[]")) return;

        // Parse JSON manually to avoid Jackson dependency issues
        var cleanText = text.replaceAll("[\\[\\] ]", "");
        var books = cleanText.split("},\\{");
        for (var book : books) {
            if (book.contains("\"id\":")) {
                var id = book.replaceAll(".*\"id\":(\\d+).*", "$1");
                request.delete(baseUrl() + "api/books/" + id);
            }
        }
    }

    void createBook(String title, String author, String isbn, Integer year) {
        var json = String.format(
            "{\"title\":\"%s\",\"author\":\"%s\"%s%s}",
            title, author,
            isbn != null ? ",\"isbn\":\"" + isbn + "\"" : "",
            year != null ? ",\"publicationYear\":" + year : ""
        );

        var response = page.request().post(baseUrl() + "api/books",
            RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData(json));

        assertThat(response).isOK();
    }
}
