package sk409.youtube.requests;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class PlaylistStoreRequest {

    @NotNull
    @Size(min = 1, max = 256)
    private String name;

    @NotNull
    @Size(min = 1, max = 1024)
    private String overview;

    @NotNull
    private Long channelId;

}