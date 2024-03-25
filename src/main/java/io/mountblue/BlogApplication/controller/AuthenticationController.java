package io.mountblue.BlogApplication.controller;

import io.mountblue.BlogApplication.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthenticationController {

//    private UserServiceImplementation userService;
    private UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
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
        boolean checkIfSaved = userService.saveUser(name, password, email);
        if (!checkIfSaved) {
            model.addAttribute("error", "User already exists.");
            return "signupPage";
        }
        return "redirect:/login-page";
    }
}
