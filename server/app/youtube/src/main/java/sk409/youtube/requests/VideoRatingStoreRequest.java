package sk409.youtube.requests;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class VideoRatingStoreRequest {

    @NotNull
    private Long videoId;

    @NotNull
    private Long ratingId;

}