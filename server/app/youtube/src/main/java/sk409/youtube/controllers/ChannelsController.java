package sk409.youtube.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sk409.youtube.models.Channel;
import sk409.youtube.models.Subscriber;
import sk409.youtube.models.User;
import sk409.youtube.query.QueryComponents;
import sk409.youtube.query.specifications.SubscriberSpecifications;
import sk409.youtube.requests.ChannelStoreRequest;
import sk409.youtube.requests.ChannelSubscriptionRequest;
import sk409.youtube.responses.ChannelResponse;
import sk409.youtube.services.ChannelService;
import sk409.youtube.services.SubscriberService;
import sk409.youtube.services.UserService;

@Controller
@RequestMapping("/channels")
public class ChannelsController {

    private final ChannelService channelService;
    private final SubscriberService subscriberService;
    private final UserService userService;

    public ChannelsController(final ChannelService channelService, final SubscriberService subscriberService,
            final UserService userService) {
        this.channelService = channelService;
        this.subscriberService = subscriberService;
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
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        final User user = _user.get();
        final Channel channel = channelService.save(request.getName(), user.getId());
        return new ResponseEntity<Channel>(channel, HttpStatus.OK);
    }

    @GetMapping("/last_selected")
    @ResponseBody
    public ResponseEntity<ChannelResponse> lastSelected(final Principal principal) {
        final String username = principal.getName();
        final Optional<User> _user = userService.findByUsername(username);
        if (!_user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        final User user = _user.get();
        final Optional<Channel> _channel = channelService.findByUserId(user.getId());
        if (!_channel.isPresent()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        final Channel channel = _channel.get();
        final ChannelResponse response = new ChannelResponse(channel);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/subscription")
    @ResponseBody
    public ResponseEntity<List<ChannelResponse>> subscription(
            @Validated @ModelAttribute final ChannelSubscriptionRequest request, final BindingResult bindingResult,
            final Principal principal) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final String username = principal.getName();
        final Optional<User> _user = userService.findByUsername(username);
        if (!_user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        final User user = _user.get();
        final SubscriberSpecifications subscriberSpecifications = new SubscriberSpecifications();
        subscriberSpecifications.setUserIdEqual(user.getId());
        subscriberSpecifications.setChannelIdNotIn(request.getExcludedChannelIds());
        final QueryComponents<Subscriber> subscriberQueryComponents = new QueryComponents<>();
        subscriberQueryComponents.setSpecifications(subscriberSpecifications);
        final Optional<List<Subscriber>> _subscribers = subscriberService.findAll(subscriberQueryComponents);
        final List<Long> channelIds = _subscribers.map(subscribers -> subscribers.stream()
                .map(subscriber -> subscriber.getChannelId()).collect(Collectors.toList())).orElse(new ArrayList<>());
        final Optional<List<Channel>> _channels = channelService.findByIdIn(channelIds);
        final List<ChannelResponse> channelResponses = _channels.map(
                channels -> channels.stream().map(channel -> new ChannelResponse(channel)).collect(Collectors.toList()))
                .orElse(new ArrayList<>());
        return new ResponseEntity<>(channelResponses, HttpStatus.OK);
    }

}