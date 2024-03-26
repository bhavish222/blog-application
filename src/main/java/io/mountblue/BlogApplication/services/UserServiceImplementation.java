package io.mountblue.BlogApplication.services;

import io.mountblue.BlogApplication.entity.Role;
import io.mountblue.BlogApplication.entity.User;
import io.mountblue.BlogApplication.repository.RoleRepository;
import io.mountblue.BlogApplication.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImplementation implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public UserServiceImplementation(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean saveUser(String name, String password, String email) {
        if (userRepository.existsByEmail(email)) {
            return false;
        }
        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        String encryptedPassword = "{bcrypt}" + passwordEncoder().encode(password);
        newUser.setPassword(encryptedPassword);
        userRepository.save(newUser);
        Role role = new Role();
        User registeredUser = userRepository.findByName(newUser.getName());
        role.setRole("ROLE_AUTHOR");
        role.setUsername(registeredUser.getName());
        role.setRoleId(registeredUser.getId());
        roleRepository.save(role);
        return true;
    }

    @Override
    public User findUserByName(String loggedInUser) {
        return userRepository.findUserByName(loggedInUser);
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
