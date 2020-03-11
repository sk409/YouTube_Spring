package sk409.youtube.controllers;

import java.security.Principal;
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

import sk409.youtube.graph.builders.VideoCommentGraphBuilder;
import sk409.youtube.models.User;
import sk409.youtube.models.VideoComment;
import sk409.youtube.requests.VideoCommentFetchNextCommentsRequest;
import sk409.youtube.requests.VideoCommentStoreRequest;
import sk409.youtube.responses.UserResponse;
import sk409.youtube.responses.VideoCommentResponse;
import sk409.youtube.services.UserService;
import sk409.youtube.services.VideoCommentService;
import sk409.youtube.specifications.VideoCommentSpecifications;

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
        final Optional<User> _user = userService.findByUsername(username);
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
            @Validated @ModelAttribute VideoCommentFetchNextCommentsRequest request,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final VideoCommentSpecifications specifications = new VideoCommentSpecifications();
        specifications.setParentIdEqual(5L);
        final List<VideoComment> videoComments = videoCommentService.findAll(specifications,
                VideoCommentGraphBuilder.watch);
        final List<VideoCommentResponse> responses = videoComments.stream().map(videoComment -> {
            final VideoCommentResponse response = new VideoCommentResponse(videoComment);
            final UserResponse userResponse = new UserResponse(videoComment.getUser());
            response.setUser(userResponse);
            final List<VideoCommentResponse> childResponses = videoComment.getChildren().stream()
                    .map(child -> new VideoCommentResponse(child)).collect(Collectors.toList());
            response.setChildren(childResponses);
            return response;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(responses, HttpStatus.OK);

        // final Long oldBefore = request.getOldBefore();
        // if (oldBefore == null) {
        // final List<VideoComment> videoComments = videoCommentService
        // .findByVideoIdAndParentIdIsNullOrderByIdDescLimit(request.getVideoId(),
        // request.getLimit());
        // return new ResponseEntity<>(videoComments, HttpStatus.OK);
        // } else {
        // final List<VideoComment> videoComments = videoCommentService
        // .findByVideoIdLessThanIdAndParentIdIsNullOrderByIdDescLimit(request.getVideoId(),
        // request.getOldBefore(), request.getLimit());
        // return new ResponseEntity<>(videoComments, HttpStatus.OK);
        // }
    }

}