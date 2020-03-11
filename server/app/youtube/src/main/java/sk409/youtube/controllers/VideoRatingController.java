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
import sk409.youtube.models.VideoRating;
import sk409.youtube.query.QueryComponents;
import sk409.youtube.query.specifications.UserSpecifications;
import sk409.youtube.query.specifications.VideoRatingSpecifications;
import sk409.youtube.requests.VideoRatingDestroyRequest;
import sk409.youtube.requests.VideoRatingStoreRequest;
import sk409.youtube.services.UserService;
import sk409.youtube.services.VideoRatingService;

@Controller
@RequestMapping("/video_rating")
public class VideoRatingController {

    private final UserService userService;
    private final VideoRatingService videoRatingService;

    public VideoRatingController(final UserService userService, final VideoRatingService videoRatingService) {
        this.userService = userService;
        this.videoRatingService = videoRatingService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<VideoRating> store(@Validated @RequestBody final VideoRatingStoreRequest request,
            final BindingResult bindingResult, final Principal principal) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult);
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
        final VideoRating videoRating = videoRatingService.save(user.getId(), request.getVideoId(),
                request.getRatingId());
        return new ResponseEntity<>(videoRating, HttpStatus.OK);
    }

    @DeleteMapping
    @ResponseBody
    public ResponseEntity<VideoRating> destory(@Validated @ModelAttribute final VideoRatingDestroyRequest request,
            final BindingResult bindingResult, final Principal principal) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult);
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
        final VideoRatingSpecifications videoRatingSpecifications = new VideoRatingSpecifications();
        videoRatingSpecifications.setUserIdEqual(user.getId());
        videoRatingSpecifications.setVideoIdEqual(request.getVideoId());
        final QueryComponents<VideoRating> videoRatingQueryComponents = new QueryComponents<>();
        videoRatingQueryComponents.setSpecifications(videoRatingSpecifications);
        final Optional<VideoRating> _videoRating = videoRatingService.findOne(videoRatingQueryComponents);
        if (!_videoRating.isPresent()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        final VideoRating videoRating = _videoRating.get();
        videoRatingService.delete(videoRating);
        return new ResponseEntity<>(videoRating, HttpStatus.OK);
    }

}