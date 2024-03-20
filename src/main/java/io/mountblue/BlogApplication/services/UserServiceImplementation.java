package io.mountblue.BlogApplication.services;

import io.mountblue.BlogApplication.entity.User;
import io.mountblue.BlogApplication.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImplementation implements UserService{
    private UserRepository userRepository;

    public UserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
