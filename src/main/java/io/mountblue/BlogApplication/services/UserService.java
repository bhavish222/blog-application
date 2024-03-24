package io.mountblue.BlogApplication.services;

import io.mountblue.BlogApplication.entity.User;
import java.util.List;

public interface UserService {
    List<User> findAllUsers();
    boolean saveUser(String name, String email, String password);
}
