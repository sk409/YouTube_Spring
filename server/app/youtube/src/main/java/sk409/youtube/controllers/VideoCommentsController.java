package sk409.youtube.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sk409.youtube.models.VideoComment;
import sk409.youtube.requests.VideoCommentFetchNextCommentsRequest;
import sk409.youtube.services.VideoCommentService;

@Controller
@RequestMapping("/video_comments")
public class VideoCommentsController {

    private final VideoCommentService videoCommentService;

    public VideoCommentsController(final VideoCommentService videoCommentService) {
        this.videoCommentService = videoCommentService;
    }

    @GetMapping("/next_comments")
    @ResponseBody
    public ResponseEntity<List<VideoComment>> fetchNextComments(
            @Validated @ModelAttribute VideoCommentFetchNextCommentsRequest request,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final Long oldBefore = request.getOldBefore();
        if (oldBefore == null) {
            final List<VideoComment> videoComments = videoCommentService
                    .findByVideoIdOrderByIdDescLimit(request.getVideoId(), request.getLimit());
            return new ResponseEntity<>(videoComments, HttpStatus.OK);
        } else {
            final List<VideoComment> videoComments = videoCommentService.findByVideoIdLessThanIdOrderByIdDescLimit(
                    request.getVideoId(), request.getOldBefore(), request.getLimit());
            return new ResponseEntity<>(videoComments, HttpStatus.OK);
        }
    }

}