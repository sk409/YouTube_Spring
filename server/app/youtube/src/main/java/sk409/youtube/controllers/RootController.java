package sk409.youtube.controllers;

import java.security.Principal;
import java.util.List;

import com.google.gson.Gson;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import sk409.youtube.models.Channel;
import sk409.youtube.models.User;
import sk409.youtube.services.ChannelService;
import sk409.youtube.services.UserService;

@Controller
@RequestMapping("/")
public class RootController {

    // private ChannelService channelService;
    // private UserService userService;

    // public RootController(ChannelService channelService, UserService userService) {
    //     this.channelService = channelService;
    //     this.userService = userService;
    // }

    @GetMapping
    public ModelAndView root(ModelAndView mav, Principal principal) {
        if (principal != null) {
            System.out.println(principal.getName());
        }
        // if (principal != null) {
        //     final String username = principal.getName();
        //     final User user = userService.findByUsername(username);
        //     if (user == null) {
        //         mav.setStatus(HttpStatus.BAD_REQUEST);
        //         return mav;
        //     }
        //     final List<Channel> channels = channelService.findByUserId(user.getId());
        //     System.out.println(channels);
        //     if (channels != null && channels.size() != 0 ){
        //         final Gson gson = new Gson();
        //         mav.addObject("channelJSON", gson.toJson(channels.get(0)));
        //     }
        // }
        mav.setViewName("index");
        return mav;
    }

}