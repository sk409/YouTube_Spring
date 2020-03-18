package sk409.youtube.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sk409.youtube.models.Playlist;
import sk409.youtube.requests.PlaylistStoreRequest;
import sk409.youtube.services.PlaylistService;

@Controller
@RequestMapping("/playlists")
public class PlaylistsController {

    private PlaylistService playlistService;

    public PlaylistsController(final PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Playlist> store(@Validated @RequestBody final PlaylistStoreRequest request,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final Playlist playlist = playlistService.save(request.getName(), request.getOverview(),
                request.getChannelId());
        return new ResponseEntity<>(playlist, HttpStatus.OK);
    }

}