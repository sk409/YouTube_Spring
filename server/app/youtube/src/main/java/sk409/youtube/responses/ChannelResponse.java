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
    private Long userId;

    @JsonBackReference("UserResponse")
    private UserResponse user;

    @JsonManagedReference("channel")
    private List<VideoResponse> videos;

    public ChannelResponse(final Channel channel) {
        id = channel.getId();
        name = channel.getName();
        profileImagePath = channel.getProfileImagePath();
        userId = channel.getUserId();
    }

}