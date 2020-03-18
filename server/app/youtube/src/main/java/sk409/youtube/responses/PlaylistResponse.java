package sk409.youtube.responses;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import sk409.youtube.models.Playlist;

@Data
public class PlaylistResponse {

    private Long id;
    private String name;
    private String overview;
    private Long channelId;

    private Long videoCount;

    @JsonBackReference("Channel")
    private ChannelResponse channel;

    private VideoResponse firstVideo;

    public PlaylistResponse(final Playlist playlist) {
        id = playlist.getId();
        name = playlist.getName();
        overview = playlist.getOverview();
        channelId = playlist.getChannelId();
    }

}