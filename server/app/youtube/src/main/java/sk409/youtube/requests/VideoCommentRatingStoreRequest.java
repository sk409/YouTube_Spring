package sk409.youtube.requests;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class VideoCommentRatingStoreRequest {

    @NotNull
    private Long videoCommentId;

    @NotNull
    private Long ratingId;

}