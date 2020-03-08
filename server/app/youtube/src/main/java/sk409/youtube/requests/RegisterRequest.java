package sk409.youtube.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

public class RegisterRequest {

    @NotNull
    @Size(min = 1, max = 256)
    @Getter
    @Setter
    private String username;

    @NotNull
    @Size(min = 1, max = 256)
    @Getter
    @Setter
    private String nickname;

    @NotNull
    @Getter
    @Setter
    private String password;

    @NotNull
    @Size(min = 1, max = 256)
    @Email
    @Getter
    @Setter
    private String email;

}