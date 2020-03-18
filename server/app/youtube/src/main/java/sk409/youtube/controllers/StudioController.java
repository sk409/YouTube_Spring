package sk409.youtube.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import sk409.youtube.graph.builders.ChannelGraphBuilder;
import sk409.youtube.graph.builders.EntityGraphBuilder;
import sk409.youtube.graph.builders.VideoGraphBuilder;
import sk409.youtube.models.Channel;
import sk409.youtube.models.Playlist;
import sk409.youtube.models.Rating;
import sk409.youtube.models.User;
import sk409.youtube.models.Video;
import sk409.youtube.models.VideoComment;
import sk409.youtube.models.Video_;
import sk409.youtube.query.QueryComponents;
import sk409.youtube.query.specifications.VideoCommentSpecifications;
import sk409.youtube.query.specifications.VideoSpecifications;
import sk409.youtube.responses.ChannelResponse;
import sk409.youtube.responses.PlaylistResponse;
import sk409.youtube.responses.UserResponse;
import sk409.youtube.responses.VideoResponse;
import sk409.youtube.services.ChannelService;
import sk409.youtube.services.JSONService;
import sk409.youtube.services.PlaylistService;
import sk409.youtube.services.UserService;
import sk409.youtube.services.VideoCommentService;
import sk409.youtube.services.VideoService;

@Controller
@RequestMapping("/studio")
public class StudioController {

    private final ChannelService channelService;
    private final JSONService jsonService;
    private final PlaylistService playlistService;
    private final UserService userService;
    private final VideoCommentService videoCommentService;
    private final VideoService videoService;

    public StudioController(final ChannelService channelService, final JSONService jsonService,
            final PlaylistService playlistService, final UserService userService,
            final VideoCommentService videoCommentService, final VideoService videoService) {
        this.channelService = channelService;
        this.jsonService = jsonService;
        this.playlistService = playlistService;
        this.userService = userService;
        this.videoCommentService = videoCommentService;
        this.videoService = videoService;
    }

    @GetMapping("channels/{channelUniqueId}/playlists")
    public ModelAndView channelPlaylists(@PathVariable("channelUniqueId") final String channelUniqueId,
            final Principal principal, final ModelAndView mav) {
        final EntityGraphBuilder<Channel> channelGraphBuilder = ChannelGraphBuilder.user;
        final Optional<Channel> _channel = channelService.findByUniqueId(channelUniqueId, channelGraphBuilder);
        if (!_channel.isPresent()) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return mav;
        }
        final Channel channel = _channel.get();
        final String username = principal.getName();
        final Optional<User> _user = userService.findByUsername(username);
        if (!_user.isPresent()) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return mav;
        }
        final User user = _user.get();
        if (channel.getUserId() != user.getId()) {
            mav.setViewName("errors/permission");
            return mav;
        }
        final ChannelResponse channelResponse = new ChannelResponse(channel);
        final UserResponse userResponse = new UserResponse(channel.getUser());
        channelResponse.setUser(userResponse);
        final Optional<List<Playlist>> _playlists = playlistService.findByChannelId(channel.getId());
        final Optional<List<PlaylistResponse>> _playlistResponses = _playlists.map(playlists -> playlists.stream()
                .map(playlist -> new PlaylistResponse(playlist)).collect(Collectors.toList()));
        channelResponse.setPlaylists(_playlistResponses.orElse(new ArrayList<>()));
        final String channelResponseJSON = jsonService.toJSON(channelResponse);
        mav.addObject("channelJSON", channelResponseJSON);
        mav.setViewName("studio/channels/playlists");
        return mav;
    }

    @GetMapping("channels/{channelUniqueId}/videos/upload")
    public ModelAndView channelVideos(@PathVariable("channelUniqueId") final String channelUniqueId,
            final Principal principal, final ModelAndView mav) {
        final EntityGraphBuilder<Channel> channelGraphBuilder = ChannelGraphBuilder.user;
        final Optional<Channel> _channel = channelService.findByUniqueId(channelUniqueId, channelGraphBuilder);
        if (!_channel.isPresent()) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return mav;
        }
        final Channel channel = _channel.get();
        final String username = principal.getName();
        final Optional<User> _user = userService.findByUsername(username);
        if (!_user.isPresent()) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return mav;
        }
        final User user = _user.get();
        if (channel.getUserId() != user.getId()) {
            mav.setViewName("errors/permission");
            return mav;
        }
        final ChannelResponse channelResponse = new ChannelResponse(channel);
        final UserResponse userResponse = new UserResponse(channel.getUser());
        channelResponse.setUser(userResponse);
        final VideoSpecifications videoSpecifications = new VideoSpecifications();
        videoSpecifications.setChannelIdEqual(channel.getId());
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
        mav.setViewName("studio/channels/videos/upload");
        return mav;
    }

    @GetMapping("videos/{videoUniqueId}/edit")
    public ModelAndView videoEdit(@PathVariable("videoUniqueId") final String videoUniqueId, final Principal principal,
            final ModelAndView mav) {
        final Optional<Video> _video = videoService.findByUniqueId(videoUniqueId, Video_.CHANNEL);
        if (!_video.isPresent()) {
            mav.setViewName("errors/" + HttpStatus.INTERNAL_SERVER_ERROR);
            return mav;
        }
        final Video video = _video.get();
        final Channel channel = video.getChannel();
        final String username = principal.getName();
        final Optional<User> _user = userService.findByUsername(username);
        if (!_user.isPresent()) {
            mav.setViewName("errors/" + HttpStatus.INTERNAL_SERVER_ERROR);
            return mav;
        }
        final User user = _user.get();
        if (channel.getUserId() != user.getId()) {
            mav.setViewName("errors/permission");
            return mav;
        }
        final VideoResponse videoResponse = new VideoResponse(video);
        final String videoResponseJSON = jsonService.toJSON(videoResponse);
        mav.addObject("videoJSON", videoResponseJSON);
        mav.setViewName("studio/videos/edit");
        return mav;
    }

}