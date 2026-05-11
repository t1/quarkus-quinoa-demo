/**
 * End-to-End tests using Playwright.
 *
 * These integration tests verify the complete application stack including the UI.
 * They use `@QuarkusIntegrationTest` which starts the full application before running tests.
 *
 * ## Running Tests
 *
 * ```bash
 * # Run all integration tests
 * mvn verify
 *
 * # Run only E2E tests
 * mvn verify -Dit.test='*IT'
 *
 * # Run specific test class
 * mvn verify -Dit.test=BookCrudIT
 * ```
 *
 * ## Test Structure
 *
 * - {@link com.example.e2e.PlaywrightTestBase} - Base class with Playwright setup
 * - {@link com.example.e2e.UINavigationIT} - UI layout and navigation tests
 * - {@link com.example.e2e.BookCrudIT} - CRUD operations tests
 *
 * ## Prerequisites
 *
 * Playwright browsers must be installed before running tests:
 * ```bash
 * mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install chromium"
 * ```
 */
package com.example.e2e;
