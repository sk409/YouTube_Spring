package sk409.youtube.controllers;

import java.security.Principal;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sk409.youtube.models.User;
import sk409.youtube.services.SubscriberService;
import sk409.youtube.services.UserService;

@Controller
@RequestMapping("/users")
public class UsersController {

    private SubscriberService subscriberService;
    private UserService userService;

    public UsersController(final SubscriberService subscriberService, final UserService userService) {
        this.subscriberService = subscriberService;
        this.userService = userService;
    }

    @GetMapping("/subscription_count")
    @ResponseBody
    public ResponseEntity<Long> subscriptionCount(final Principal principal) {
        final String username = principal.getName();
        final Optional<User> _user = userService.findByUsername(username);
        if (!_user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        final User user = _user.get();
        final Long subscriptionCount = subscriberService.countByUserId(user.getId());
        return new ResponseEntity<>(subscriptionCount, HttpStatus.OK);
    }

}