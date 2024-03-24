package io.mountblue.BlogApplication.controller;

import io.mountblue.BlogApplication.entity.User;
import io.mountblue.BlogApplication.services.UserServiceImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AuthenticationController {

    private UserServiceImplementation userServiceImplementation;

    public AuthenticationController(UserServiceImplementation userServiceImplementation) {
        this.userServiceImplementation = userServiceImplementation;
    }

    @GetMapping("/login-page")
    public String loginPage() {
        return "loginPage";
    }
    @GetMapping("/signup")
    public String signup() {
        return "signupPage";
    }
    @PostMapping("/register")
    public String register(
            @RequestParam(name = "username") String name,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "email") String email,
            Model model
    ) {
        boolean checkIfSaved = userServiceImplementation.saveUser(name, password, email);
        if (!checkIfSaved) {
            model.addAttribute("error", "User already exists.");
            return "signupPage";
        }
        return "redirect:/login-page";
    }
}
