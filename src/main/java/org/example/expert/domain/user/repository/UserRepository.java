package org.example.expert.domain.user.repository;

import org.example.expert.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByNickname(String nickname);

    @Query("SELECT u FROM User u WHERE u.nicknameHash = :hash AND u.nickname = :nickname")
    Optional<User> findByNickname(@Param("nickname") String nickname, @Param("hash") Long hash);
}
