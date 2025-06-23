package io.hahn.bookspaceback.repository;

import io.hahn.bookspaceback.entity.Book;
import io.hahn.bookspaceback.entity.enums.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,Long>, JpaSpecificationExecutor<Book> {

    @Query("""
        SELECT b FROM Book b
        WHERE b.id <> :bookId AND (
            LOWER(b.author) = LOWER(:author)
            OR EXISTS (
                SELECT g FROM b.genres g
                WHERE g IN :genres
            )
        )
    """)
    Page<Book> findSimilarBooks(@Param("bookId") Long bookId, @Param("author") String author, @Param("genres") List<Genre> genres, Pageable pageable);


    @Query("""
        SELECT b FROM Book b
        WHERE b.isFeatured = TRUE
    """)
    Page<Book> findAllByIsFeaturedTrue(Pageable pageable);

    @Query("""
        SELECT rl.book FROM ReadingList rl
        WHERE rl.addedAt >= :cutoffDate
          AND rl.rating IS NOT NULL
          AND rl.rating >= 3
        GROUP BY rl.book
        ORDER BY AVG(rl.rating) DESC, COUNT(rl.id) DESC
    """)
    Page<Book> findAllByPopularity(@Param("cutoffDate") LocalDateTime cutoffDate, Pageable pageable);

}
