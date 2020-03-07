package sk409.youtube.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
import sk409.youtube.services.VideoService;

@Controller
@RequestMapping("/channels/{channelId}/videos")
public class VideosController {

    private final ChannelService channelService;
    private final JSONService jsonService;
    private final UserService userService;
    private final VideoCommentService videoCommentService;
    private final VideoService videoService;

    public VideosController(ChannelService channelService, JSONService jsonService, UserService userService, VideoCommentService videoCommentService, VideoService videoService) {
        this.channelService = channelService;
        this.jsonService = jsonService;
        this.userService = userService;
        this.videoCommentService = videoCommentService;
        this.videoService = videoService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Video> store(@PathVariable("channelId") Long channelId, @Validated @ModelAttribute VideoStoreRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            final Video video = videoService.save(request.getTitle(), request.getOverview(), request.getDuration(), request.getUniqueId(), channelId, request.getVideo(), request.getThumbnail());
            return new ResponseEntity<>(video, HttpStatus.OK);
        } catch (IOException exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/upload")
    public ModelAndView showUploadForm(@PathVariable("channelId") Long channelId, ModelAndView mav,
            Principal principal) {
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
        final String channelJSON = jsonService.toJSON(channel);
        final String videosJSON = videos == null ? "[]" : jsonService.toJSON(videos);
        mav.addObject("channelJSON", channelJSON);
        mav.addObject("videosJSON", videosJSON);
        mav.setViewName("channels/videos/upload");
        return mav;
    }

}