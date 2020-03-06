package sk409.youtube.controllers;

import java.security.Principal;

import sk409.youtube.models.User;
import sk409.youtube.services.UserService;

public class Controller {

    private final UserService userService;

    public Controller(UserService userService) {
        this.userService = userService;
    }

    public User getUserByPrincipal(Principal principal) {
        final String username = principal.getName();
        final User user = userService.findByUsername(username);
        return user;
    }

}