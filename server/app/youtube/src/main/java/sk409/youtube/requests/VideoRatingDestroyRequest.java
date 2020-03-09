package sk409.youtube.requests;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class VideoRatingDestroyRequest {

    @NotNull
    private Long videoId;

}