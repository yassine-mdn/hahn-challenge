package io.hahn.bookspaceback.repository;

import io.hahn.bookspaceback.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    Optional<User> findByUserName(String userName);
    Optional<User> findByEmail(String email);
    Optional<User> findByUserNameOrEmail(String userName, String email);
    @Transactional
    @Modifying
    void deleteByUserName(String userName);
}
