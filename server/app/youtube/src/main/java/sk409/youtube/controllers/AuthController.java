package sk409.youtube.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import sk409.youtube.models.User;
import sk409.youtube.requests.RegisterRequest;
import sk409.youtube.services.UserService;

@Controller
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login() {
        return "todos";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    @ResponseBody
    public User register(@Validated @RequestBody final RegisterRequest registerRequest, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return null;
        }
        return userService.registerUser(registerRequest.getUsername(), registerRequest.getNickname(), registerRequest.getPassword(),
        registerRequest.getEmail());
    }

}