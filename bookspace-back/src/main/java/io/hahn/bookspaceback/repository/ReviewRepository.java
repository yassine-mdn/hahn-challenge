package io.hahn.bookspaceback.repository;

import io.hahn.bookspaceback.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Boolean existsByReadingList_Id(Long readingListId);

    Page<Review> findAllByReadingList_Book_Id(Long readingListBookId, Pageable pageable);

    Page<Review> findAllByReadingList_User_UserName(String readingListUserUserName, Pageable pageable);
}
