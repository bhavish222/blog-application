package io.mountblue.BlogApplication.services;

import io.mountblue.BlogApplication.entity.User;
import io.mountblue.BlogApplication.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Override
    public boolean saveUser(String name, String password, String email) {
        if(userRepository.existsByEmail(email)) {
            return false;
        }
        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        String encryptedPassword = "{bcrypt}"+passwordEncoder().encode(password);
        newUser.setPassword(encryptedPassword);
        userRepository.save(newUser);
        return true;
    }
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
