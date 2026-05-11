package com.example.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.microsoft.playwright.options.LoadState.NETWORKIDLE;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@QuarkusIntegrationTest
public abstract class PlaywrightTestBase {

    static Playwright playwright;
    static Browser browser;
    protected BrowserContext context;
    protected Page page;

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
        page.waitForLoadState(NETWORKIDLE);
        page.waitForSelector("h1");
    }

    void deleteAllBooks() {
        var request = page.request();
        var response = request.get(baseUrl() + "api/books");

        if (!response.ok()) return;

        try {
            var books = new ObjectMapper().readValue(response.text(), Map[].class);
            for (var book : books) {
                request.delete(baseUrl() + "api/books/" + book.get("id"));
            }
        } catch (Exception e) {
            throw new RuntimeException("could not parse books response", e);
        }
    }

    void createBook(String title, String author, String isbn, Integer year) {
        try {
            var fields = new HashMap<String, Object>();
            fields.put("title", title);
            fields.put("author", author);
            if (isbn != null) fields.put("isbn", isbn);
            if (year != null) fields.put("publicationYear", year);

            var json = new ObjectMapper().writeValueAsString(fields);
            var response = page.request().post(baseUrl() + "api/books",
                RequestOptions.create()
                    .setHeader("Content-Type", "application/json")
                    .setData(json));

            assertThat(response).isOK();
        } catch (Exception e) {
            throw new RuntimeException("could not serialize book", e);
        }
    }
}
