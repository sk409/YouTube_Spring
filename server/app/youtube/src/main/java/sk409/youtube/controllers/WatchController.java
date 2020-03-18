package sk409.youtube.controllers;

import java.security.Principal;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import sk409.youtube.models.Channel;
import sk409.youtube.models.Rating;
import sk409.youtube.models.Subscriber;
import sk409.youtube.models.User;
import sk409.youtube.models.Video;
import sk409.youtube.models.VideoComment;
import sk409.youtube.models.VideoRating;
import sk409.youtube.models.Video_;
import sk409.youtube.query.QueryComponents;
import sk409.youtube.query.specifications.SubscriberSpecifications;
import sk409.youtube.query.specifications.VideoCommentSpecifications;
import sk409.youtube.query.specifications.VideoRatingSpecifications;
import sk409.youtube.responses.ChannelResponse;
import sk409.youtube.responses.SubscriberResponse;
import sk409.youtube.responses.UserResponse;
import sk409.youtube.responses.VideoRatingResponse;
import sk409.youtube.responses.VideoResponse;
import sk409.youtube.services.JSONService;
import sk409.youtube.services.SubscriberService;
import sk409.youtube.services.UserService;
import sk409.youtube.services.VideoCommentService;
import sk409.youtube.services.VideoRatingService;
import sk409.youtube.services.VideoService;

@Controller
@RequestMapping("/watch")
public class WatchController {

    private final JSONService jsonService;
    private final SubscriberService subscriberService;
    private final UserService userService;
    private final VideoCommentService videoCommentService;
    private final VideoRatingService videoRatingService;
    private final VideoService videoService;

    public WatchController(final JSONService jsonService, final SubscriberService subscriberService,
            final UserService userService, final VideoCommentService videoCommentService,
            final VideoRatingService videoRatingService, final VideoService videoService) {
        this.jsonService = jsonService;
        this.subscriberService = subscriberService;
        this.userService = userService;
        this.videoCommentService = videoCommentService;
        this.videoRatingService = videoRatingService;
        this.videoService = videoService;
    }

    @GetMapping
    public ModelAndView watch(@RequestParam("v") final String videoUniqueId, final Principal principal,
            final ModelAndView mav) {
        final String username = principal.getName();
        final Optional<User> _user = userService.findByUsername(username);
        if (!_user.isPresent()) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return mav;
        }
        final User user = _user.get();
        final UserResponse userResponse = new UserResponse(user);
        final Optional<Video> _video = videoService.findByUniqueId(videoUniqueId, Video_.CHANNEL, Video_.RATING);
        if (!_video.isPresent()) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return mav;
        }
        final Video video = _video.get();
        final Channel channel = video.getChannel();
        final VideoResponse videoResponse = new VideoResponse(video);
        final ChannelResponse channelResponse = new ChannelResponse(channel);
        channelResponse.setUser(userResponse);
        final Long subscriberCount = subscriberService.countByChannelId(channel.getId());
        channelResponse.setSubscriberCount(subscriberCount);
        videoResponse.setChannel(channelResponse);
        final VideoCommentSpecifications videoCommentSpecifications = new VideoCommentSpecifications();
        videoCommentSpecifications.setParentIdIsNull(true);
        videoCommentSpecifications.setVideoIdEqual(video.getId());
        final QueryComponents<VideoComment> videoCommentQuery = new QueryComponents<>();
        videoCommentQuery.setSpecifications(videoCommentSpecifications);
        final Optional<Long> commentCount = videoCommentService.findAll(videoCommentQuery)
                .map(videoComments -> Long.valueOf(videoComments.size()));
        videoResponse.setCommentCount(commentCount.orElse(0L));
        final Long highRatingCount = video.getRating().stream().filter(
                videoRating -> videoRating.getRatingId() == Rating.highId && videoRating.getUserId() != user.getId())
                .count();
        videoResponse.setHighRatingCount(highRatingCount);
        final Long lowRatingCount = video.getRating().stream().filter(
                videoRating -> videoRating.getRatingId() == Rating.lowId && videoRating.getUserId() != user.getId())
                .count();
        videoResponse.setLowRatingCount(lowRatingCount);
        final String videoJSON = jsonService.toJSON(videoResponse);
        final VideoRatingSpecifications videoRatingSpecifications = new VideoRatingSpecifications();
        videoRatingSpecifications.setUserIdEqual(user.getId());
        videoRatingSpecifications.setVideoIdEqual(video.getId());
        final QueryComponents<VideoRating> videoRatingQueryComponents = new QueryComponents<>();
        videoRatingQueryComponents.setSpecifications(videoRatingSpecifications);
        final Optional<String> _userRatingJSON = videoRatingService.findOne(videoRatingQueryComponents)
                .map(videoRating -> jsonService.toJSON(new VideoRatingResponse(videoRating)));
        final SubscriberSpecifications userSubscriberSpecifications = new SubscriberSpecifications();
        userSubscriberSpecifications.setChannelIdEqual(channel.getId());
        userSubscriberSpecifications.setUserIdEqual(user.getId());
        final QueryComponents<Subscriber> subscriberQueryComponents = new QueryComponents<>();
        subscriberQueryComponents.setSpecifications(userSubscriberSpecifications);
        final Optional<String> _userSubscriberJSON = subscriberService.findOne(subscriberQueryComponents)
                .map(userSubscriber -> jsonService.toJSON(new SubscriberResponse(userSubscriber)));
        mav.addObject("videoJSON", videoJSON);
        mav.addObject("userRatingJSON", _userRatingJSON.orElse(null));
        mav.addObject("userSubscriberJSON", _userSubscriberJSON.orElse(null));
        mav.setViewName("watch");
        video.setViews(video.getViews() + 1);
        videoService.save(video);
        return mav;
    }

}