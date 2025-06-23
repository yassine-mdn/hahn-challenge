package io.hahn.bookspaceback.repository;

import io.hahn.bookspaceback.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
