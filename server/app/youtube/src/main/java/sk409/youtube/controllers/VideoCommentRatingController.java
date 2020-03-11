package sk409.youtube.controllers;

import java.security.Principal;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sk409.youtube.models.User;
import sk409.youtube.models.VideoCommentRating;
import sk409.youtube.query.QueryComponents;
import sk409.youtube.query.specifications.UserSpecifications;
import sk409.youtube.requests.VideoCommentRatingDestroyRequest;
import sk409.youtube.requests.VideoCommentRatingStoreRequest;
import sk409.youtube.services.UserService;
import sk409.youtube.services.VideoCommentRatingService;

@Controller
@RequestMapping("/video_comment_rating")
public class VideoCommentRatingController {

    private final UserService userService;
    private final VideoCommentRatingService videoCommentRatingService;

    public VideoCommentRatingController(final UserService userSerivce,
            final VideoCommentRatingService videoCommentRatingService) {
        this.userService = userSerivce;
        this.videoCommentRatingService = videoCommentRatingService;
    }

    @DeleteMapping
    @ResponseBody
    public ResponseEntity<VideoCommentRating> destroy(
            @Validated @ModelAttribute final VideoCommentRatingDestroyRequest request,
            final BindingResult bindingResult, Principal principal) {
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
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final User user = _user.get();
        final Optional<VideoCommentRating> _videoCommentRating = videoCommentRatingService.delete(user.getId(),
                request.getVideoCommentId());
        return _videoCommentRating.isPresent() ? new ResponseEntity<>(_videoCommentRating.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<VideoCommentRating> store(
            @Validated @RequestBody final VideoCommentRatingStoreRequest request, final BindingResult bindingResult,
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
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final User user = _user.get();
        final VideoCommentRating videoCommentRating = videoCommentRatingService.save(user.getId(),
                request.getVideoCommentId(), request.getRatingId());
        return new ResponseEntity<>(videoCommentRating, HttpStatus.OK);
    }
}