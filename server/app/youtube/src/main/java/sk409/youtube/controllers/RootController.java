package sk409.youtube.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import sk409.youtube.models.Channel;
import sk409.youtube.models.User;
import sk409.youtube.responses.ChannelResponse;
import sk409.youtube.services.ChannelService;
import sk409.youtube.services.JSONService;
import sk409.youtube.services.UserService;

@Controller
@RequestMapping("/")
public class RootController {

    private final ChannelService channelService;
    private final JSONService jsonService;
    private final UserService userService;

    public RootController(final ChannelService channelService, final JSONService jsonService,
            final UserService userService) {
        this.channelService = channelService;
        this.jsonService = jsonService;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView root(final Principal principal, final ModelAndView mav) {
        final String username = principal.getName();
        final Optional<User> _user = userService.findByUsername(username);
        if (!_user.isPresent()) {
            mav.setStatus(HttpStatus.BAD_REQUEST);
            return mav;
        }
        final User user = _user.get();
        final Optional<List<Channel>> _subscriptionChannels = channelService.findSubscription(user.getId());
        final Optional<List<ChannelResponse>> _subscriptionChannelResponses = _subscriptionChannels
                .map(subscriptionChannels -> subscriptionChannels.stream()
                        .map(subscriptionChannel -> new ChannelResponse(subscriptionChannel))
                        .collect(Collectors.toList()));
        final Optional<String> _subscriptionChannelResponsesJSON = _subscriptionChannelResponses
                .map(subscriptionChannelResponses -> jsonService.toJSON(subscriptionChannelResponses));
        mav.addObject("subscriptionChannelsJSON", _subscriptionChannelResponsesJSON.orElse(null));
        mav.setViewName("index");
        return mav;
    }

}