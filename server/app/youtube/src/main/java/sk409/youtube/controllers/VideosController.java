package sk409.youtube.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
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

import sk409.youtube.models.Channel;
import sk409.youtube.models.User;
import sk409.youtube.models.Video;
import sk409.youtube.requests.VideoStoreRequest;
import sk409.youtube.services.ChannelService;
import sk409.youtube.services.JSONService;
import sk409.youtube.services.UserService;
import sk409.youtube.services.VideoCommentService;
import sk409.youtube.services.VideoRatingService;
import sk409.youtube.services.VideoService;

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
    public ModelAndView watch(@RequestParam("v") final String videoUniqueId, ModelAndView mav) {
        final Video video = videoService.findByUniqueId(videoUniqueId);
        if (video == null) {
            mav.setStatus(HttpStatus.BAD_REQUEST);
            mav.setViewName("errors/bad_request");
            return mav;
        }
        mav.addObject("video", video);
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
        final Channel channel = channelService.findById(channelId);
        if (channel == null) {
            mav.setStatus(HttpStatus.BAD_REQUEST);
            return mav;
        }
        final String username = principal.getName();
        final User user = userService.findByUsername(username);
        if (user == null) {
            mav.setStatus(HttpStatus.BAD_REQUEST);
            return mav;
        }
        if (channel.getUser().getId() != user.getId()) {
            mav.setViewName("errors/permission");
            return mav;
        }
        final List<Video> videos = videoService.findByChannelId(channelId);
        final List<Long> videoIds = videos.stream().map(video -> video.getId()).collect(Collectors.toList());
        final List<VideoCommentService.SummaryCountByVideoIdGroupByVideoId> videoCommentCounts = videoCommentService
                .countByVideoIdInGroupByVideoId(videoIds.toArray(new Long[videoIds.size()]));
        final List<VideoRatingService.SummaryCountByVideoIdGroupByVideoIdAndRatingId> videoRatingCounts = videoRatingService
                .countByVideoIdInGroupByVideoIdAndRatingId(videoIds.toArray(new Long[videoIds.size()]));
        final String channelJSON = jsonService.toJSON(channel);
        final String videosJSON = videos == null ? "[]" : jsonService.toJSON(videos);
        final String videoCommentCountsJSON = jsonService.toJSON(videoCommentCounts);
        final String videoRatingCountsJSON = jsonService.toJSON(videoRatingCounts);
        mav.addObject("channelJSON", channelJSON);
        mav.addObject("videosJSON", videosJSON);
        mav.addObject("videoCommentCountsJSON", videoCommentCountsJSON);
        mav.addObject("videoRatingCountsJSON", videoRatingCountsJSON);
        mav.setViewName("channels/videos/upload");
        return mav;
    }

}