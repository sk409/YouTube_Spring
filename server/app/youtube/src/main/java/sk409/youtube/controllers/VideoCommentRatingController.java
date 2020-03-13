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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sk409.youtube.models.User;
import sk409.youtube.models.VideoCommentRating;
import sk409.youtube.requests.VideoCommentRatingStoreRequest;
import sk409.youtube.requests.VideoCommentRatingUpdateRequest;
import sk409.youtube.responses.VideoCommentRatingResponse;
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

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<VideoCommentRatingResponse> destroy(@PathVariable("id") final Long id) {
        final Optional<VideoCommentRating> _videoCommentRating = videoCommentRatingService.delete(id);
        if (!_videoCommentRating.isPresent()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        final VideoCommentRatingResponse videoCommentRatingResponse = new VideoCommentRatingResponse(
                _videoCommentRating.get());
        return new ResponseEntity<>(videoCommentRatingResponse, HttpStatus.OK);
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
        final Optional<User> _user = userService.findByUsername(username);
        if (!_user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final User user = _user.get();
        final VideoCommentRating videoCommentRating = videoCommentRatingService.save(user.getId(),
                request.getVideoCommentId(), request.getRatingId());
        return new ResponseEntity<>(videoCommentRating, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<VideoCommentRatingResponse> update(@PathVariable("id") final Long id,
            @Validated @ModelAttribute final VideoCommentRatingUpdateRequest request, final BindingResult bindingResult,
            final Principal principal) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final Optional<VideoCommentRating> _videoCommentRating = videoCommentRatingService.findById(id);
        if (!_videoCommentRating.isPresent()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        final VideoCommentRating videoCommentRating = _videoCommentRating.get();
        videoCommentRating.setRatingId(request.getRatingId());
        videoCommentRating.setVideoCommentId(request.getVideoCommentId());
        videoCommentRatingService.save(videoCommentRating);
        final VideoCommentRatingResponse videoCommentRatingResponse = new VideoCommentRatingResponse(
                videoCommentRating);
        return new ResponseEntity<>(videoCommentRatingResponse, HttpStatus.OK);
    }

}