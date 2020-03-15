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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import sk409.youtube.models.Channel;
import sk409.youtube.models.Subscriber;
import sk409.youtube.models.User;
import sk409.youtube.models.Video;
import sk409.youtube.query.QueryComponents;
import sk409.youtube.query.specifications.SubscriberSpecifications;
import sk409.youtube.requests.ChannelStoreRequest;
import sk409.youtube.requests.ChannelSubscriptionRequest;
import sk409.youtube.responses.ChannelResponse;
import sk409.youtube.responses.SubscriberResponse;
import sk409.youtube.responses.VideoResponse;
import sk409.youtube.services.ChannelService;
import sk409.youtube.services.JSONService;
import sk409.youtube.services.SubscriberService;
import sk409.youtube.services.UserService;
import sk409.youtube.services.VideoService;

@Controller
@RequestMapping("/channels")
public class ChannelsController {

    private final ChannelService channelService;
    private final JSONService jsonService;
    private final SubscriberService subscriberService;
    private final UserService userService;
    private final VideoService videoService;

    public ChannelsController(final ChannelService channelService, final JSONService jsonService,
            final SubscriberService subscriberService, final UserService userService, final VideoService videoService) {
        this.channelService = channelService;
        this.jsonService = jsonService;
        this.subscriberService = subscriberService;
        this.userService = userService;
        this.videoService = videoService;
    }

    @GetMapping("/{channelUniqueId}")
    public ModelAndView show(@PathVariable final String channelUniqueId, final Principal principal,
            final ModelAndView mav) {
        final String username = principal.getName();
        final Optional<User> _user = userService.findByUsername(username);
        if (!_user.isPresent()) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return mav;
        }
        final User user = _user.get();
        final Optional<Channel> _channel = channelService.findByUniqueId(channelUniqueId);
        if (!_channel.isPresent()) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return mav;
        }
        final Channel channel = _channel.get();
        final ChannelResponse channelResponse = new ChannelResponse(channel);
        final Long subscriberCount = subscriberService.countByChannelId(channel.getId());
        channelResponse.setSubscriberCount(subscriberCount);
        final String channelResponseJSON = jsonService.toJSON(channelResponse);
        final Optional<List<Video>> _newVideos = videoService.findNewVideosChannel(channel.getId(), 12);
        final Optional<List<VideoResponse>> _newVideoResponses = _newVideos.map(newVideos -> newVideos.stream()
                .map(newVideo -> new VideoResponse(newVideo)).collect(Collectors.toList()));
        final Optional<String> _newVideoResponsesJSON = _newVideoResponses
                .map(newVideoResponses -> jsonService.toJSON(newVideoResponses));
        final Optional<List<Video>> _popularVideos = videoService.findPopularChannel(channel.getId(), 12);
        final Optional<List<VideoResponse>> _popularVideoResponses = _popularVideos.map(popularVideos -> popularVideos
                .stream().map(popularVideo -> new VideoResponse(popularVideo)).collect(Collectors.toList()));
        final Optional<String> _popularVideoResponsesJSON = _popularVideoResponses
                .map(popularVideoResponses -> jsonService.toJSON(popularVideoResponses));
        final SubscriberSpecifications userSubscriberSpecifications = new SubscriberSpecifications();
        userSubscriberSpecifications.setChannelIdEqual(channel.getId());
        userSubscriberSpecifications.setUserIdEqual(user.getId());
        final Optional<Subscriber> _userSubscriber = subscriberService.findOne(userSubscriberSpecifications);
        final Optional<SubscriberResponse> _userSubscriberResponse = _userSubscriber
                .map(userSubscriber -> new SubscriberResponse(userSubscriber));
        final Optional<String> _userSubscriberJSON = _userSubscriberResponse
                .map(userSubscriber -> jsonService.toJSON(userSubscriber));
        mav.addObject("channelJSON", channelResponseJSON);
        mav.addObject("newVideosJSON", _newVideoResponsesJSON.orElse(null));
        mav.addObject("popularVideosJSON", _popularVideoResponsesJSON.orElse(null));
        mav.addObject("userSubscriberJSON", _userSubscriberJSON.orElse(null));
        mav.setViewName("channels/show");
        return mav;
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
        final Channel channel = channelService.save(request.getName(), request.getUniqueId(), user.getId());
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
        subscriberQueryComponents.setLimit(request.getLimit());
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