package io.hahn.bookspaceback.repository;

import io.hahn.bookspaceback.entity.ReadingList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadingListRepository extends JpaRepository<ReadingList, Long> {

    Page<ReadingList> findAllByUser_UserName(String username, Pageable pageable);

    Boolean existsByBook_IdAndUser_Id(Long bookId, String userId);

}
