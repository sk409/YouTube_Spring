package sk409.youtube.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import sk409.youtube.graph.builders.ChannelGraphBuilder;
import sk409.youtube.graph.builders.EntityGraphBuilder;
import sk409.youtube.graph.builders.VideoGraphBuilder;
import sk409.youtube.models.Channel;
import sk409.youtube.models.Rating;
import sk409.youtube.models.Subscriber;
import sk409.youtube.models.User;
import sk409.youtube.models.Video;
import sk409.youtube.models.VideoComment;
import sk409.youtube.models.VideoRating;
import sk409.youtube.models.Video_;
import sk409.youtube.query.QueryComponents;
import sk409.youtube.query.specifications.ChannelSpecifications;
import sk409.youtube.query.specifications.SubscriberSpecifications;
import sk409.youtube.query.specifications.VideoCommentSpecifications;
import sk409.youtube.query.specifications.VideoRatingSpecifications;
import sk409.youtube.query.specifications.VideoSpecifications;
import sk409.youtube.requests.VideoNewChannelRequest;
import sk409.youtube.requests.VideoOldChannelRequest;
import sk409.youtube.requests.VideoPopularChannelRequest;
import sk409.youtube.requests.VideoRecommendationRequest;
import sk409.youtube.requests.VideoStoreRequest;
import sk409.youtube.responses.ChannelResponse;
import sk409.youtube.responses.SubscriberResponse;
import sk409.youtube.responses.UserResponse;
import sk409.youtube.responses.VideoRatingResponse;
import sk409.youtube.responses.VideoResponse;
import sk409.youtube.services.ChannelService;
import sk409.youtube.services.JSONService;
import sk409.youtube.services.SubscriberService;
import sk409.youtube.services.UserService;
import sk409.youtube.services.VideoCommentService;
import sk409.youtube.services.VideoRatingService;
import sk409.youtube.services.VideoService;

@Controller
public class VideosController {

    private final ChannelService channelService;
    private final JSONService jsonService;
    private final SubscriberService subscriberService;
    private final UserService userService;
    private final VideoCommentService videoCommentService;
    private final VideoRatingService videoRatingService;
    private final VideoService videoService;

    public VideosController(final ChannelService channelService, final JSONService jsonService,
            final SubscriberService subscriberService, final UserService userService,
            final VideoCommentService videoCommentService, final VideoRatingService videoRatingService,
            final VideoService videoService) {
        this.channelService = channelService;
        this.jsonService = jsonService;
        this.subscriberService = subscriberService;
        this.userService = userService;
        this.videoCommentService = videoCommentService;
        this.videoRatingService = videoRatingService;
        this.videoService = videoService;
    }

    @GetMapping("/watch")
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

    @GetMapping("/channels/{channelId}/videos/upload")
    public ModelAndView showUploadForm(@PathVariable("channelId") final Long channelId, final Principal principal,
            final ModelAndView mav) {
        final ChannelSpecifications channelSpecifications = new ChannelSpecifications();
        channelSpecifications.setIdEqual(channelId);
        final EntityGraphBuilder<Channel> channelGraphBuilder = ChannelGraphBuilder.owner;
        final QueryComponents<Channel> channelQueryComponents = new QueryComponents<>();
        channelQueryComponents.setSpecifications(channelSpecifications);
        channelQueryComponents.setEntityGraphBuilder(channelGraphBuilder);
        final Optional<Channel> _channel = channelService.findOne(channelQueryComponents);
        if (!_channel.isPresent()) {
            mav.setStatus(HttpStatus.BAD_REQUEST);
            return mav;
        }
        final Channel channel = _channel.get();
        final String username = principal.getName();
        final Optional<User> _user = userService.findByUsername(username);
        if (!_user.isPresent()) {
            mav.setStatus(HttpStatus.BAD_REQUEST);
            return mav;
        }
        final User user = _user.get();
        if (channel.getUserId() != user.getId()) {
            mav.setViewName("errors/permission");
            return mav;
        }
        final ChannelResponse channelResponse = new ChannelResponse(channel);
        final VideoSpecifications videoSpecifications = new VideoSpecifications();
        videoSpecifications.setChannelIdEqual(channelId);
        final EntityGraphBuilder<Video> videoGraphBuilder = VideoGraphBuilder.rating;
        final QueryComponents<Video> videoQueryComponents = new QueryComponents<>();
        videoQueryComponents.setSpecifications(videoSpecifications);
        videoQueryComponents.setEntityGraphBuilder(videoGraphBuilder);
        final Optional<List<Video>> _videos = videoService.findAll(videoQueryComponents);
        final List<Video> videos = _videos.isPresent() ? _videos.get() : new ArrayList<>();
        final List<Long> videoIds = videos.stream().map(video -> video.getId()).collect(Collectors.toList());
        final VideoCommentSpecifications videoCommentSpecifications = new VideoCommentSpecifications();
        videoCommentSpecifications.setVideoIdIn(videoIds);
        final QueryComponents<VideoComment> videoCommentQueryComponents = new QueryComponents<>();
        videoCommentQueryComponents.setSpecifications(videoCommentSpecifications);
        final Optional<List<VideoComment>> _videoComments = videoCommentService.findAll(videoCommentQueryComponents);
        final List<VideoComment> videoComments = _videoComments.isPresent() ? _videoComments.get() : new ArrayList<>();
        final Map<Long, List<VideoComment>> videoCommentsMap = videoComments.stream()
                .collect(Collectors.groupingBy(VideoComment::getVideoId));
        final List<VideoResponse> videoResponses = videos.stream().map(video -> {
            final VideoResponse videoResponse = new VideoResponse(video);
            final List<VideoComment> vc = videoCommentsMap.get(video.getId());
            videoResponse.setCommentCount(vc == null ? 0L : Long.valueOf(vc.size()));
            final Long highRatingCount = video.getRating().stream()
                    .filter(videoRating -> videoRating.getRatingId() == Rating.highId).count();
            final Long lowRatingCount = video.getRating().stream()
                    .filter(videoRating -> videoRating.getRatingId() == Rating.lowId).count();
            final Long ratingCount = highRatingCount + lowRatingCount;
            if (ratingCount == 0L) {
                videoResponse.setHighRatingRate(0f);
            } else {
                videoResponse.setHighRatingRate(highRatingCount.floatValue() / ratingCount.floatValue());
            }
            return videoResponse;
        }).collect(Collectors.toList());
        channelResponse.setVideos(videoResponses);
        final String channelJSON = jsonService.toJSON(channelResponse);
        mav.addObject("channelJSON", channelJSON);
        mav.setViewName("channels/videos/upload");
        return mav;
    }

    @GetMapping("/videos/new_channel")
    @ResponseBody
    public ResponseEntity<List<VideoResponse>> newChannel(
            @Validated @ModelAttribute final VideoNewChannelRequest request, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final VideoSpecifications videoSpecifications = new VideoSpecifications();
        videoSpecifications.setIdLessThan(request.getOldBeforeId());
        final QueryComponents<Video> option = new QueryComponents<>();
        option.setSpecifications(videoSpecifications);
        final Optional<List<Video>> _videos = videoService.findNewChannel(request.getChannelId(), request.getLimit(),
                option);
        final Optional<List<VideoResponse>> _videoResponses = _videos
                .map(videos -> videos.stream().map(video -> new VideoResponse(video)).collect(Collectors.toList()));
        final List<VideoResponse> videoResponses = _videoResponses.orElse(new ArrayList<>());
        return new ResponseEntity<>(videoResponses, HttpStatus.OK);
    }

    @GetMapping("/videos/old_channel")
    @ResponseBody
    public ResponseEntity<List<VideoResponse>> oldChannel(
            @Validated @ModelAttribute final VideoOldChannelRequest request, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final VideoSpecifications videoSpecifications = new VideoSpecifications();
        videoSpecifications.setIdGreaterThan(request.getNewAfter());
        final QueryComponents<Video> option = new QueryComponents<>();
        option.setSpecifications(videoSpecifications);
        final Optional<List<Video>> _videos = videoService.findOldChannel(request.getChannelId(), request.getLimit(),
                option);
        final Optional<List<VideoResponse>> _videoResponses = _videos
                .map(videos -> videos.stream().map(video -> new VideoResponse(video)).collect(Collectors.toList()));
        final List<VideoResponse> videoResponses = _videoResponses.orElse(new ArrayList<>());
        return new ResponseEntity<>(videoResponses, HttpStatus.OK);
    }

    @GetMapping("/videos/popular_channel")
    @ResponseBody
    public ResponseEntity<List<VideoResponse>> popularChannel(
            @Validated @ModelAttribute final VideoPopularChannelRequest request, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final Optional<List<Video>> _videos = videoService.findPopularChannel(request.getChannelId(),
                request.getLimit());
        final List<VideoResponse> videoResponses = _videos
                .map(videos -> videos.stream().map(video -> new VideoResponse(video)).collect(Collectors.toList()))
                .orElse(new ArrayList<>());
        return new ResponseEntity<>(videoResponses, HttpStatus.OK);
    }

    @PostMapping("/videos")
    @ResponseBody
    public ResponseEntity<Video> store(@Validated @ModelAttribute final VideoStoreRequest request,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            final Video video = videoService.save(request.getTitle(), request.getOverview(), request.getDuration(),
                    request.getUniqueId(), request.getChannelId(), request.getVideo(), request.getThumbnail());
            return new ResponseEntity<>(video, HttpStatus.OK);
        } catch (IOException exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/videos/recommendation")
    @ResponseBody
    public ResponseEntity<List<VideoResponse>> recommendataion(
            @Validated @ModelAttribute final VideoRecommendationRequest request, final BindingResult bindingResult,
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
        final QueryComponents<Video> videoQueryComponents = new QueryComponents<>();
        videoQueryComponents.setLimit(request.getLimit());
        if (request.getExcludedIds() != null) {
            final VideoSpecifications videoSpecifications = new VideoSpecifications();
            videoSpecifications.setIdNotIn(request.getExcludedIds());
            videoQueryComponents.setSpecifications(videoSpecifications);
        }
        final Optional<List<Video>> _videos = videoService.findRecommendation(user.getId(), videoQueryComponents);
        final List<VideoResponse> videoResponses = _videos
                .map(videos -> videos.stream().map(video -> new VideoResponse(video)).collect(Collectors.toList()))
                .orElse(new ArrayList<>());
        return new ResponseEntity<>(videoResponses, HttpStatus.OK);
    }

}