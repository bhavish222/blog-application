package io.mountblue.BlogApplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {
    @GetMapping("/login-page")
    public String loginPage() {
        return "loginPage";
    }
}
