package sk409.youtube.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

import sk409.youtube.models.User;

@Data
public class UserResponse {

    private Long id;
    private String username;
    private String nickname;
    private String password;
    private String email;
    private String profileImagePath;

    private Long subscriptionCount;

    @JsonManagedReference("user")
    private List<ChannelResponse> channels;

    public UserResponse(final User user) {
        id = user.getId();
        username = user.getUsername();
        nickname = user.getNickname();
        password = user.getPassword();
        email = user.getEmail();
        profileImagePath = user.getProfileImagePath();
    }

}