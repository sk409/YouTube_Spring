package sk409.youtube.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sk409.youtube.models.Channel;
import sk409.youtube.models.User;
import sk409.youtube.requests.ChannelStoreRequest;
import sk409.youtube.services.ChannelService;
import sk409.youtube.services.UserService;

@Controller
@RequestMapping("/channels")
public class ChannelsController {

    private final ChannelService channelService;
    private final UserService userService;

    public ChannelsController(final ChannelService channelService, final UserService userService) {
        this.channelService = channelService;
        this.userService = userService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Channel> store(@Validated @RequestBody final ChannelStoreRequest request,
            final BindingResult bindingResult, final Principal principal) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final String username = principal.getName();
        final Optional<User> _user = userService.findByUsername(username);
        if (!_user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final User user = _user.get();
        final Channel channel = channelService.save(request.getName(), user.getId());
        return new ResponseEntity<Channel>(channel, HttpStatus.OK);
    }

    @GetMapping("/last_selected")
    @ResponseBody
    public ResponseEntity<Channel> lastSelected(Principal principal) {
        final String username = principal.getName();
        final Optional<User> _user = userService.findByUsername(username);
        if (!_user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final User user = _user.get();
        final Optional<List<Channel>> _channels = channelService.findByUserId(user.getId());
        if (!_channels.isPresent()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(_channels.get().get(0), HttpStatus.OK);
    }

}