package sk409.youtube.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class RegisterRequest {

    @NotNull
    @Size(min = 1, max = 256)
    private String username;

    @NotNull
    @Size(min = 1, max = 256)
    private String nickname;

    @NotNull
    private String password;

    @NotNull
    @Size(min = 1, max = 256)
    @Email
    private String email;

}