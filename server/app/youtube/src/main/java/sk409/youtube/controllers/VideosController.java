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
import sk409.youtube.models.User;
import sk409.youtube.models.Video;
import sk409.youtube.models.VideoComment;
import sk409.youtube.requests.VideoStoreRequest;
import sk409.youtube.responses.ChannelResponse;
import sk409.youtube.responses.VideoResponse;
import sk409.youtube.services.ChannelService;
import sk409.youtube.services.JSONService;
import sk409.youtube.services.UserService;
import sk409.youtube.services.VideoCommentService;
import sk409.youtube.services.VideoRatingService;
import sk409.youtube.services.VideoService;
import sk409.youtube.query.QueryComponents;
import sk409.youtube.query.specifications.ChannelSpecifications;
import sk409.youtube.query.specifications.UserSpecifications;
import sk409.youtube.query.specifications.VideoCommentSpecifications;
import sk409.youtube.query.specifications.VideoSpecifications;

@Controller
public class VideosController {

    private final ChannelService channelService;
    private final JSONService jsonService;
    private final UserService userService;
    private final VideoCommentService videoCommentService;
    private final VideoRatingService videoRatingService;
    private final VideoService videoService;

    public VideosController(final ChannelService channelService, final JSONService jsonService,
            final UserService userService, final VideoCommentService videoCommentService,
            final VideoRatingService videoRatingService, final VideoService videoService) {
        this.channelService = channelService;
        this.jsonService = jsonService;
        this.userService = userService;
        this.videoCommentService = videoCommentService;
        this.videoRatingService = videoRatingService;
        this.videoService = videoService;
    }

    @GetMapping("/watch")
    public ModelAndView watch(@RequestParam("v") final String videoUniqueId, final ModelAndView mav,
            final Principal principal) {
        final String username = principal.getName();
        final UserSpecifications userSpecifications = new UserSpecifications();
        userSpecifications.setUsernameEqual(username);
        final QueryComponents<User> userQueryComponents = new QueryComponents<>();
        userQueryComponents.setSpecifications(userSpecifications);
        final Optional<User> _user = userService.findOne(userQueryComponents);
        if (!_user.isPresent()) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return mav;
        }
        final User user = _user.get();
        final VideoSpecifications videoSpecifications = new VideoSpecifications();
        videoSpecifications.setUniqueIdEqual(videoUniqueId);
        final QueryComponents<Video> videoQueryComponents = new QueryComponents<>();
        videoQueryComponents.setSpecifications(videoSpecifications);
        final Optional<Video> _video = videoService.findOne(videoQueryComponents);
        if (!_video.isPresent()) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return mav;
        }
        final Video video = _video.get();
        video.setViews(video.getViews() + 1);
        videoService.save(video);
        final String videoJSON = jsonService.toJSON(video);
        final VideoCommentService.SummaryCount videoCommentCount = videoCommentService
                .countByVideoIdGroupByVideoId(video.getId());
        final List<VideoRatingService.SummaryCount> videoRatingCounts = videoRatingService
                .countByVideoIdAndNotUserIdGroupByVideoIdAndRatingId(video.getId(), user.getId());
        final Optional<String> _highRatingJSON = videoRatingCounts.stream()
                .filter(videoRatingCount -> videoRatingCount.getRatingId() == Rating.highId).findFirst()
                .map(highRatingCount -> jsonService.toJSON(highRatingCount));
        final Optional<String> _lowRatingJSON = videoRatingCounts.stream()
                .filter(videoRatingCount -> videoRatingCount.getRatingId() == Rating.lowId).findFirst()
                .map(lowRatingCount -> jsonService.toJSON(lowRatingCount));
        // final Optional<String> _userRatingJSON = videoRatingService
        // .findFirstByUserIdAndVideoId(user.getId(), video.getId())
        // .map(videoRating -> jsonService.toJSON(videoRating));
        final Optional<String> _userRatingJSON = Optional.ofNullable(null);
        mav.addObject("video", video);
        mav.addObject("videoJSON", videoJSON);
        mav.addObject("videoCommentCount", videoCommentCount.getCount());
        mav.addObject("highRatingJSON", _highRatingJSON.orElse(null));
        mav.addObject("lowRatingJSON", _lowRatingJSON.orElse(null));
        mav.addObject("userRatingJSON", _userRatingJSON.orElse(null));
        mav.setViewName("watch");
        return mav;
    }

    @PostMapping("/channels/{channelId}/videos")
    @ResponseBody
    public ResponseEntity<Video> store(@PathVariable("channelId") final Long channelId,
            @Validated @ModelAttribute final VideoStoreRequest request, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            final Video video = videoService.save(request.getTitle(), request.getOverview(), request.getDuration(),
                    request.getUniqueId(), channelId, request.getVideo(), request.getThumbnail());
            return new ResponseEntity<>(video, HttpStatus.OK);
        } catch (IOException exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/channels/{channelId}/videos/upload")
    public ModelAndView showUploadForm(@PathVariable("channelId") final Long channelId, final ModelAndView mav,
            final Principal principal) {
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
        final UserSpecifications userSpecifications = new UserSpecifications();
        userSpecifications.setUsernameEqual(username);
        final QueryComponents<User> userQueryComponents = new QueryComponents<>();
        userQueryComponents.setSpecifications(userSpecifications);
        final Optional<User> _user = userService.findOne(userQueryComponents);
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
        final EntityGraphBuilder<Video> videoGraphBuilder = VideoGraphBuilder.reaction;
        final QueryComponents<Video> videoQueryComponents = new QueryComponents<>();
        videoQueryComponents.setSpecifications(videoSpecifications);
        videoQueryComponents.setEntityGraphBuilder(videoGraphBuilder);
        final Optional<List<Video>> _videos = videoService.findAll(videoQueryComponents);
        final List<Video> videos = _videos.isPresent() ? _videos.get() : new ArrayList<>();
        final Long[] videoIds = videos.stream().map(video -> video.getId()).collect(Collectors.toList())
                .toArray(new Long[] {});
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
            videoResponse.setCommentCount(vc == null ? 0 : vc.size());
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

}