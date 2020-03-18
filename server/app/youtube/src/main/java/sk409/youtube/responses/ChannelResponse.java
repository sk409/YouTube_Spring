package sk409.youtube.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import sk409.youtube.models.Channel;

@Data
public class ChannelResponse {

    private Long id;
    private String name;
    private String profileImagePath;
    private String uniqueId;
    private Long userId;

    private Long subscriberCount;

    @JsonBackReference("UserResponse")
    private UserResponse user;

    @JsonManagedReference("channel")
    private List<VideoResponse> videos;

    @JsonManagedReference("channel")
    private List<PlaylistResponse> playlists;

    public ChannelResponse(final Channel channel) {
        id = channel.getId();
        name = channel.getName();
        profileImagePath = channel.getProfileImagePath();
        uniqueId = channel.getUniqueId();
        userId = channel.getUserId();
    }

}