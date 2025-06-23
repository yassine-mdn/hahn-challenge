package io.hahn.bookspaceback.specification;

import io.hahn.bookspaceback.entity.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSpec {

    public static Specification<Book> search(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;
            String searchTerm = "%" + keyword.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("title")), searchTerm),
                    cb.like(cb.lower(root.get("author")), searchTerm)
            );
        };
    }
}
