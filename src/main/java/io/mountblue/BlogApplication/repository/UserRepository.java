package io.mountblue.BlogApplication.repository;

import io.mountblue.BlogApplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    User findByName(String name);
    User findUserByName(String user);
    List<User> findAuthorByIdIn(List<Long> userIds);
}
