package sk409.youtube.controllers;

import java.io.IOException;
import java.nio.file.Paths;
import java.security.Principal;

import com.google.gson.Gson;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import sk409.youtube.models.Channel;
import sk409.youtube.models.User;
import sk409.youtube.models.Video;
import sk409.youtube.requests.VideoStoreRequest;
import sk409.youtube.services.ChannelService;
import sk409.youtube.services.FileService;
import sk409.youtube.services.UserService;
import sk409.youtube.services.VideoService;

@Controller
@RequestMapping("/channels/{channelId}/videos")
public class VideosController {

    private static final String videoPath = "src/main/resources/static/videos";

    private final ChannelService channelService;
    private final FileService fileService;
    private final UserService userService;
    private final VideoService videoService;

    public VideosController(ChannelService channelService, FileService fileService, UserService userService, VideoService videoService) {
        this.channelService = channelService;
        this.fileService = fileService;
        this.userService = userService;
        this.videoService = videoService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Video> store(@PathVariable("channelId") Long channelId, @Validated @ModelAttribute VideoStoreRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final Channel channel = channelService.findById(channelId);
        if (channel == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final Video video = videoService.save(request.getTitle(), request.getOverview(), request.getUniqueId(), channel);
        final MultipartFile videoFile = request.getVideo();
        final String path = Paths.get(videoPath, String.valueOf(channelId),String.valueOf(video.getId()), videoFile.getOriginalFilename()).toString();
        try {
            fileService.write(path, videoFile.getBytes());
        } catch(IOException exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(video, HttpStatus.OK);
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
        final Gson gson = new Gson();
        final String channelJSON = gson.toJson(channel);
        mav.addObject("channelJSON", channelJSON);
        mav.setViewName("channels/videos/upload");
        return mav;
    }

}