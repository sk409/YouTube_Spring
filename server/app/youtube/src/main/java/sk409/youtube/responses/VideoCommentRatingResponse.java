package sk409.youtube.responses;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import sk409.youtube.models.VideoCommentRating;

@Data
public class VideoCommentRatingResponse {

    private Long id;
    private Long userId;
    private Long videoCommentId;
    private Long ratingId;

    @JsonBackReference("UserResponse")
    private UserResponse user;

    @JsonBackReference("VideoCommentResponse")
    private VideoCommentResponse videoComment;

    @JsonBackReference("RatingResponse")
    private RatingResponse rating;

    public VideoCommentRatingResponse(final VideoCommentRating videoCommentRating) {
        id = videoCommentRating.getId();
        userId = videoCommentRating.getUserId();
        videoCommentId = videoCommentRating.getVideoCommentId();
        ratingId = videoCommentRating.getRatingId();
    }

}