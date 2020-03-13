package sk409.youtube.controllers;

import java.security.Principal;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sk409.youtube.models.Subscriber;
import sk409.youtube.models.User;
import sk409.youtube.requests.SubscriberStoreRequest;
import sk409.youtube.responses.SubscriberResponse;
import sk409.youtube.services.SubscriberService;
import sk409.youtube.services.UserService;

@Controller
@RequestMapping("/subscribers")
public class SubscribersController {

    private final SubscriberService subscriberService;
    private final UserService userService;

    public SubscribersController(final SubscriberService subscriberService, final UserService userService) {
        this.subscriberService = subscriberService;
        this.userService = userService;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<SubscriberResponse> destroy(@PathVariable final Long id) {
        final Optional<Subscriber> _subscriber = subscriberService.delete(id);
        if (!_subscriber.isPresent()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        final SubscriberResponse subscriberResponse = new SubscriberResponse(_subscriber.get());
        return new ResponseEntity<>(subscriberResponse, HttpStatus.OK);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Subscriber> store(@Validated @RequestBody final SubscriberStoreRequest request,
            final BindingResult bindingResult, final Principal principal) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final String username = principal.getName();
        final Optional<User> _user = userService.findByUsername(username);
        if (!_user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        final User user = _user.get();
        final Subscriber subscriber = subscriberService.save(user.getId(), request.getChannelId());
        return new ResponseEntity<>(subscriber, HttpStatus.OK);
    }

}