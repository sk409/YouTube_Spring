package sk409.youtube.controllers;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sk409.youtube.models.User;
import sk409.youtube.models.Video;
import sk409.youtube.query.QueryComponents;
import sk409.youtube.query.specifications.VideoSpecifications;
import sk409.youtube.requests.VideoNewChannelRequest;
import sk409.youtube.requests.VideoOldChannelRequest;
import sk409.youtube.requests.VideoPopularChannelRequest;
import sk409.youtube.requests.VideoRecommendationRequest;
import sk409.youtube.requests.VideoStoreRequest;
import sk409.youtube.responses.VideoResponse;
import sk409.youtube.services.UserService;
import sk409.youtube.services.VideoService;

@Controller
@RequestMapping("/videos")
public class VideosController {

    private final UserService userService;
    private final VideoService videoService;

    public VideosController(final UserService userService, final VideoService videoService) {
        this.userService = userService;
        this.videoService = videoService;
    }

    @GetMapping("/new_channel")
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

    @GetMapping("/old_channel")
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

    @GetMapping("/popular_channel")
    @ResponseBody
    public ResponseEntity<List<VideoResponse>> popularChannel(
            @Validated @ModelAttribute final VideoPopularChannelRequest request, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final VideoSpecifications videoSpecifications = new VideoSpecifications();
        videoSpecifications.setIdNotIn(request.getExcludedIds());
        final Optional<List<Video>> _videos = videoService.findPopularChannel(request.getChannelId(),
                request.getLimit(), videoSpecifications);
        final List<VideoResponse> videoResponses = _videos
                .map(videos -> videos.stream().map(video -> new VideoResponse(video)).collect(Collectors.toList()))
                .orElse(new ArrayList<>());
        return new ResponseEntity<>(videoResponses, HttpStatus.OK);
    }

    @PostMapping
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

    @GetMapping("/recommendation")
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