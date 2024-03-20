package io.mountblue.BlogApplication.services;

import io.mountblue.BlogApplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserService {
    List<User> findAllUsers();
}
