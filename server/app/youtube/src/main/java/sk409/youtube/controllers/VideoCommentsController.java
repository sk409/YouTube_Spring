package sk409.youtube.controllers;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sk409.youtube.graph.builders.VideoCommentGraphBuilder;
import sk409.youtube.models.Rating;
import sk409.youtube.models.User;
import sk409.youtube.models.VideoComment;
import sk409.youtube.models.VideoCommentRating;
import sk409.youtube.requests.VideoCommentFetchNextCommentsRequest;
import sk409.youtube.requests.VideoCommentStoreRequest;
import sk409.youtube.responses.UserResponse;
import sk409.youtube.responses.VideoCommentRatingResponse;
import sk409.youtube.responses.VideoCommentResponse;
import sk409.youtube.services.UserService;
import sk409.youtube.services.VideoCommentRatingService;
import sk409.youtube.services.VideoCommentService;
import sk409.youtube.query.QueryComponents;
import sk409.youtube.query.specifications.UserSpecifications;
import sk409.youtube.query.specifications.VideoCommentRatingSpecifications;

@Controller
@RequestMapping("/video_comments")
public class VideoCommentsController {

    private final UserService userService;
    private final VideoCommentRatingService videoCommentRatingService;
    private final VideoCommentService videoCommentService;

    public VideoCommentsController(final UserService userService,
            final VideoCommentRatingService videoCommentRatingService, final VideoCommentService videoCommentService) {
        this.userService = userService;
        this.videoCommentRatingService = videoCommentRatingService;
        this.videoCommentService = videoCommentService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<VideoComment> store(@Validated @RequestBody final VideoCommentStoreRequest request,
            final BindingResult bindingResult, final Principal principal) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final String username = principal.getName();
        final UserSpecifications userSpecifications = new UserSpecifications();
        userSpecifications.setUsernameEqual(username);
        final QueryComponents<User> userQueryComponents = new QueryComponents<>();
        userQueryComponents.setSpecifications(userSpecifications);
        final Optional<User> _user = userService.findOne(userQueryComponents);
        if (!_user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        final User user = _user.get();
        final VideoComment videoComment = videoCommentService.save(request.getText(), request.getParentId(),
                user.getId(), request.getVideoId());
        return new ResponseEntity<>(videoComment, HttpStatus.OK);
    }

    @GetMapping("/next_comments")
    @ResponseBody
    public ResponseEntity<List<VideoCommentResponse>> fetchNextComments(
            @Validated @ModelAttribute VideoCommentFetchNextCommentsRequest request, final BindingResult bindingResult,
            final Principal principal) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final String username = principal.getName();
        final UserSpecifications userSpecifications = new UserSpecifications();
        userSpecifications.setUsernameEqual(username);
        final QueryComponents<User> userQueryComponents = new QueryComponents<>();
        userQueryComponents.setSpecifications(userSpecifications);
        final Optional<User> _user = userService.findOne(userQueryComponents);
        if (!_user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        final User user = _user.get();
        final Optional<List<VideoComment>> _videoComments = videoCommentService
                .findPopularComments(request.getVideoId(), request.getLimit(), request.getExclude(), VideoCommentGraphBuilder.watch);
        if (!_videoComments.isPresent()) {
            final List<VideoCommentResponse> empty = new ArrayList<>();
            return new ResponseEntity<>(empty, HttpStatus.OK);
        }
        final List<VideoComment> videoComments = _videoComments.get();
        final Long[] videoCommentIds = videoComments.stream().map(videoComment -> videoComment.getId())
                .collect(Collectors.toList()).toArray(new Long[] {});
        final VideoCommentRatingSpecifications videoCommentRatingSpecifications = new VideoCommentRatingSpecifications();
        videoCommentRatingSpecifications.setUserIdNotEqual(user.getId());
        videoCommentRatingSpecifications.setVideoCommentIdIn(videoCommentIds);
        final QueryComponents<VideoCommentRating> videoCommentRatingQueryComponents = new QueryComponents<>();
        videoCommentRatingQueryComponents.setSpecifications(videoCommentRatingSpecifications);
        final Optional<Map<Long, List<VideoCommentRating>>> _videoCommentRatingMap = videoCommentRatingService
                .findAll(videoCommentRatingQueryComponents).map(videoCommentRatingList -> videoCommentRatingList
                        .stream().collect(Collectors.groupingBy(VideoCommentRating::getVideoCommentId)));
        final VideoCommentRatingSpecifications videoCommentUserRatingSpecifications = new VideoCommentRatingSpecifications();
        videoCommentUserRatingSpecifications.setUserIdEqual(user.getId());
        videoCommentUserRatingSpecifications.setVideoCommentIdIn(videoCommentIds);
        final QueryComponents<VideoCommentRating> videoCommentUserRatingQueryComponents = new QueryComponents<>();
        videoCommentUserRatingQueryComponents.setSpecifications(videoCommentUserRatingSpecifications);
        final Optional<Map<Long, List<VideoCommentRating>>> _videoCommentUserRatingMap = videoCommentRatingService
                .findAll(videoCommentUserRatingQueryComponents)
                .map(userVideoCommentRatingList -> userVideoCommentRatingList.stream()
                        .collect(Collectors.groupingBy(VideoCommentRating::getVideoCommentId)));
        final List<VideoCommentResponse> videoCommentResponses = videoComments.stream().map(videoComment -> {
            final VideoCommentResponse videoCommentResponse = new VideoCommentResponse(videoComment);
            final UserResponse userResponse = new UserResponse(videoComment.getUser());
            videoCommentResponse.setUser(userResponse);
            final List<VideoCommentResponse> childResponses = videoComment.getChildren().stream()
                    .map(child -> new VideoCommentResponse(child)).collect(Collectors.toList());
            videoCommentResponse.setChildren(childResponses);
            final Optional<List<VideoCommentRating>> _videoCommentHighRatingList = _videoCommentRatingMap
                    .map(videoCommentRatingMap -> videoCommentRatingMap.get(videoComment.getId()))
                    .map(videoCommentRatingList -> videoCommentRatingList == null ? null
                            : videoCommentRatingList.stream()
                                    .filter(videoCommentRating -> videoCommentRating.getRatingId() == Rating.highId)
                                    .collect(Collectors.toList()));
            final Long highRatingCount = _videoCommentHighRatingList
                    .map(videoCOmmentHighRatingList -> videoCOmmentHighRatingList == null ? 0L
                            : videoCOmmentHighRatingList.size())
                    .orElse(0L);
            videoCommentResponse.setHighRatingCount(highRatingCount);
            final Optional<List<VideoCommentRating>> _videoCommentLowRatingList = _videoCommentRatingMap
                    .map(videoCommentRatingMap -> videoCommentRatingMap.get(videoComment.getId()))
                    .map(videoCommentRatingList -> videoCommentRatingList == null ? null
                            : videoCommentRatingList.stream()
                                    .filter(videoCommentRating -> videoCommentRating.getRatingId() == Rating.lowId)
                                    .collect(Collectors.toList()));
            final Long lowRatingCount = _videoCommentLowRatingList.map(
                    videoCOmmenLowRatingList -> videoCOmmenLowRatingList == null ? 0L : videoCOmmenLowRatingList.size())
                    .orElse(0L);
            videoCommentResponse.setLowRatingCount(lowRatingCount);
            _videoCommentUserRatingMap.ifPresent(userVideoCommentRatingMap -> {
                final List<VideoCommentRating> userVideoCommentRatingList = userVideoCommentRatingMap
                        .get(videoComment.getId());
                if (userVideoCommentRatingList == null) {
                    return;
                }
                final VideoCommentRating userVideoCommentRating = userVideoCommentRatingList.get(0);
                final VideoCommentRatingResponse userVideoCommentRatingResponse = new VideoCommentRatingResponse(
                        userVideoCommentRating);
                videoCommentResponse.setUserRating(userVideoCommentRatingResponse);
            });
            return videoCommentResponse;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(videoCommentResponses, HttpStatus.OK);
    }

}