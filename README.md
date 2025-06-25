# hahn-challenge

A minimal Goodreads clone, featuring user authentication, book browsing, reviews, reading lists, and admin management tools.



## Pages

- **Home:** Overview of featured and popular books (`/`)
- **Login:** User authentication (`/login`)
- **Sign Up:** User registration (`/sign-up`)
- **Browse Books:** List and search all books (`/browse`)
- **Book Details:** Detailed view of a book, reviews, and ratings. Users can add, update, or remove the book from their own reading list here (`/books/:id`).
- **User Profile:** View user details and reviews (`/user/:username`)
- **User Reading List:** Manage your reading list (`/user/:username/reading-list`)
- **User Settings:** Edit your profile and preferences (`/user/:username/settings`)
- **Admin - Book Management:** Admin interface for managing books (`/admin/books`)
- **Admin - User Management:** Admin interface for managing users (`/admin/users`)

## Default Users

A default admin user is provided:
- **Username:** `admin`
- **Password:** `admin`


## Project Structure

- `bookspace-back/` — Spring Boot backend (Java 17, Maven)
- `bookspace-client/` — Vite + React + TypeScript frontend

## DB structure

```mermaid
classDiagram
direction BT
class book {
   varchar(255) title
   varchar(255) author
   varchar(255) publisher
   varchar(5000) description
   varchar(255) cover_url
   boolean is_featured
   bigint id
}
class book_genres {
   bigint book_id
   varchar(255) genres
}
class reading_list {
   varchar(36) user_id
   bigint book_id
   timestamp added_at
   timestamp updated_at
   timestamp started_at
   timestamp completed_at
   varchar(255) status
   integer rating
   bigint id
}
class review {
   bigint reading_list_id
   varchar(2000) comment
   timestamp created_at
   bigint id
}
class users {
   varchar(15) username
   varchar(255) email
   varchar(72) password
   smallint role
   timestamp created_at
   timestamp updated_at
   varchar(36) id
}

book_genres --> book
reading_list --> book
reading_list --> users
review --> reading_list
```

## Backend: Spring Boot

### Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL (or compatible DB)

### Configuration

Edit `bookspace-back/src/main/resources/application.yml` to set your database credentials:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/bookspace
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver
```

- `url`: JDBC connection string to your PostgreSQL instance.
- `username`: Your DB username.
- `password`: Your DB password.

> **Note:** The default config uses `postgres`/`postgres` and expects a local DB named `bookspace`.

### Build & Run

From the `bookspace-back` directory:

```sh
# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

The backend will start on port `1001` by default (see `server.port` in `application.yml`).

- **API Docs:** The backend exposes Swagger UI at `/swagger/swagger-ui/index.html` (e.g., `http://localhost:1001/swagger/swagger-ui/index.html`).

- **Database Migrations:** Managed by Liquibase (see `spring.liquibase` in `application.yml`).

## Frontend: Vite + React

### Prerequisites

- Node.js (v18+ recommended)
- npm (v9+ recommended)

### Install Dependencies

From the `bookspace-client` directory:

```sh
npm install
```

### Development Server

```sh
npm run dev
```

- The app will be available at the URL printed in the terminal (usually `http://localhost:5173`).
