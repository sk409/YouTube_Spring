package sk409.youtube.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import sk409.youtube.models.User;
import sk409.youtube.models.VideoComment;
import sk409.youtube.query.QueryComponents;
import sk409.youtube.query.specifications.UserSpecifications;
import sk409.youtube.requests.VideoCommentNextCommentsRequest;
import sk409.youtube.requests.VideoCommentRepliesRequest;
import sk409.youtube.requests.VideoCommentStoreRequest;
import sk409.youtube.responses.VideoCommentResponse;
import sk409.youtube.services.UserService;
import sk409.youtube.services.VideoCommentService;

@Controller
@RequestMapping("/video_comments")
public class VideoCommentsController {

    private final UserService userService;
    private final VideoCommentService videoCommentService;

    public VideoCommentsController(final UserService userService, final VideoCommentService videoCommentService) {
        this.userService = userService;
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
    public ResponseEntity<List<VideoCommentResponse>> nextComments(
            @Validated @ModelAttribute VideoCommentNextCommentsRequest request, final BindingResult bindingResult,
            final Principal principal) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final Optional<List<VideoComment>> _videoComments = videoCommentService.findNextComments(request.getVideoId(),
                request.getLimit(), request.getExclude(), VideoCommentGraphBuilder.watch);
        final Optional<List<VideoCommentResponse>> _videoCommentResponses = _videoComments
                .map(videoComments -> videoCommentService.publicWatch(_videoComments.get(), principal.getName()))
                .orElse(Optional.ofNullable(null));
        final List<VideoCommentResponse> videoCommentResponses = _videoCommentResponses.orElse(new ArrayList<>());
        return new ResponseEntity<>(videoCommentResponses, HttpStatus.OK);
    }

    @GetMapping("/replies")
    @ResponseBody
    private ResponseEntity<List<VideoCommentResponse>> replies(
            @Validated @ModelAttribute final VideoCommentRepliesRequest request, final BindingResult bindingResult,
            final Principal principal) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final Optional<List<VideoComment>> _replies = videoCommentService.findReplies(request.getVideoCommentId(),
                request.getNewAfterVideoCommentId(), request.getLimit());
        final Optional<List<VideoCommentResponse>> _videoCommentResponses = _replies
                .map(videoComments -> videoCommentService.publicWatch(_replies.get(), principal.getName()))
                .orElse(Optional.ofNullable(null));
        final List<VideoCommentResponse> videoCommentResponses = _videoCommentResponses.orElse(new ArrayList<>());
        return new ResponseEntity<>(videoCommentResponses, HttpStatus.OK);
    }

}