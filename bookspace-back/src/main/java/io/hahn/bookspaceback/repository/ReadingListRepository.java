package io.hahn.bookspaceback.repository;

import io.hahn.bookspaceback.entity.ReadingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadingListRepository extends JpaRepository<ReadingList, Integer> {

}
