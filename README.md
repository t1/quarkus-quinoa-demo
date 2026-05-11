# Book Catalog

A simple book catalog management application built with [Quarkus](https://quarkus.io/) and Vue.js.

## Features

- **Backend**: RESTful API built with Quarkus REST and Hibernate ORM with Panache
- **Frontend**: Vue 3 single-page application with Vite, integrated via Quinoa
- **Database**: H2 in-memory database (auto-configured with Dev Services)
- **Full CRUD operations**: Create, read, update, and delete books
- **Sample data**: Pre-loaded with classic books
- **API Documentation**: OpenAPI 3.1 spec with [OpenAPI UI](https://github.com/t1/openapi-ui-next)

## Quick Start

Start the application in dev mode that enables live coding, i.e. code changes (frontend, REST API, whatever) are hot-reloaded:

```shell
./mvnw quarkus:dev
```

Then open:
- **Frontend**: http://localhost:8080/
- **Dev UI**: http://localhost:8080/q/dev/
- **OpenAPI UI**: http://localhost:8080/openapi-ui
- **OpenAPI Spec**: http://localhost:8080/q/openapi

## Related Guides

- Quinoa ([guide](https://quarkiverse.github.io/quarkiverse-docs/quarkus-quinoa/dev/index.html)): Develop, build, and serve your npm-compatible web applications such as React, Angular, Vue, Lit, Svelte, Astro, SolidJS, and others alongside Quarkus.
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it
- Hibernate ORM with Panache ([guide](https://quarkus.io/guides/hibernate-orm-panache)): Simplified JPA/Hibernate data access layer with active record and repository patterns
- JDBC Driver - H2 ([guide](https://quarkus.io/guides/datasource)): Connect to the H2 database via JDBC
