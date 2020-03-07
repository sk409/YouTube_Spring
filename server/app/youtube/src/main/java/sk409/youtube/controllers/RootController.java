package sk409.youtube.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class RootController {

    @GetMapping
    public ModelAndView root(ModelAndView mav) {
        mav.setViewName("index");
        return mav;
    }

}