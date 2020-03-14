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

import sk409.youtube.graph.builders.EntityGraphBuilder;
import sk409.youtube.graph.builders.VideoGraphBuilder;
import sk409.youtube.models.Channel;
import sk409.youtube.models.User;
import sk409.youtube.models.Video;
import sk409.youtube.query.QueryComponents;
import sk409.youtube.responses.ChannelResponse;
import sk409.youtube.responses.UserResponse;
import sk409.youtube.responses.VideoResponse;
import sk409.youtube.services.ChannelService;
import sk409.youtube.services.JSONService;
import sk409.youtube.services.SubscriberService;
import sk409.youtube.services.UserService;
import sk409.youtube.services.VideoService;

@Controller
@RequestMapping("/")
public class RootController {

    private final ChannelService channelService;
    private final JSONService jsonService;
    private final SubscriberService subscriberService;
    private final UserService userService;
    private final VideoService videoService;

    public RootController(final ChannelService channelService, final JSONService jsonService,
            final SubscriberService subscriberService, final UserService userService, final VideoService videoService) {
        this.channelService = channelService;
        this.jsonService = jsonService;
        this.subscriberService = subscriberService;
        this.userService = userService;
        this.videoService = videoService;
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
        // final Long subscriptionCount =
        // subscriberService.countSubscription(user.getId());
        // final UserResponse userResponse = new UserResponse(user);
        // userResponse.setSubscriptionCount(subscriptionCount);
        // final String userResponseJSON = jsonService.toJSON(userResponse);
        // final QueryComponents<Channel> channelQueryComponents = new
        // QueryComponents<>();
        // channelQueryComponents.setLimit(2);
        // final Optional<List<Channel>> _subscriptionChannels =
        // channelService.findSubscription(user.getId(),
        // channelQueryComponents);
        // final Optional<List<ChannelResponse>> _subscriptionChannelResponses =
        // _subscriptionChannels
        // .map(subscriptionChannels -> subscriptionChannels.stream()
        // .map(subscriptionChannel -> new ChannelResponse(subscriptionChannel))
        // .collect(Collectors.toList()));
        // final Optional<String> _subscriptionChannelResponsesJSON =
        // _subscriptionChannelResponses
        // .map(subscriptionChannelResponses ->
        // jsonService.toJSON(subscriptionChannelResponses));
        final EntityGraphBuilder<Video> videoGraphBuilder = VideoGraphBuilder.user;
        final QueryComponents<Video> videoQueryComponents = new QueryComponents<>();
        videoQueryComponents.setEntityGraphBuilder(videoGraphBuilder);
        videoQueryComponents.setLimit(40);
        final Optional<List<Video>> _recommendedVideos = videoService.findRecommendation(user.getId(),
                videoQueryComponents);
        final Optional<List<VideoResponse>> _recommendedVideoResponses = _recommendedVideos.map(videos -> {
            final List<VideoResponse> videoResponses = videos.stream().map(video -> {
                final VideoResponse videoResponse = new VideoResponse(video);
                final Channel channel = video.getChannel();
                final ChannelResponse channelResponse = new ChannelResponse(channel);
                channelResponse.setUser(new UserResponse(channel.getUser()));
                videoResponse.setChannel(channelResponse);
                return videoResponse;
            }).collect(Collectors.toList());
            return videoResponses;
        });
        final Optional<String> _recommendedVideoResponsesJSON = _recommendedVideoResponses
                .map(videoResponses -> jsonService.toJSON(videoResponses));
        // mav.addObject("userJSON", userResponseJSON);
        // mav.addObject("subscriptionChannelsJSON",
        // _subscriptionChannelResponsesJSON.orElse(null));
        mav.addObject("recommendedVideosJSON", _recommendedVideoResponsesJSON.orElse(null));
        mav.setViewName("index");
        return mav;
    }

}