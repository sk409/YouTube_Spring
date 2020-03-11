package sk409.youtube.responses;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import sk409.youtube.models.VideoRating;

@Data
public class VideoRatingResponse {

    private Long id;
    private Long userId;
    private Long videoId;
    private Long ratingId;

    @JsonBackReference("UserResponse")
    private UserResponse user;

    @JsonBackReference("VideoResponse")
    private VideoResponse video;

    @JsonBackReference("RatingResponse")
    private RatingResponse rating;

    public VideoRatingResponse(final VideoRating videoRating) {
        id = videoRating.getId();
        userId = videoRating.getUserId();
        videoId = videoRating.getVideoId();
        ratingId = videoRating.getRatingId();
    }

}