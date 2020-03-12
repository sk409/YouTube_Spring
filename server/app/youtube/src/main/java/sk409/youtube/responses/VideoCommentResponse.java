package sk409.youtube.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import sk409.youtube.models.VideoComment;

@Data
public class VideoCommentResponse {

    private Long id;
    private String text;
    private Long parentId;
    private Long userId;
    private Long videoId;

    private Long highRatingCount;
    private Long lowRatingCount;

    // @JsonBackReference("UserResponse")
    private UserResponse user;

    @JsonBackReference("VideoCommentResponse")
    private VideoCommentResponse parent;

    private VideoCommentRatingResponse userRating;

    @JsonManagedReference("parent")
    private List<VideoCommentResponse> children;

    public VideoCommentResponse(final VideoComment videoComment) {
        id = videoComment.getId();
        text = videoComment.getText();
        parentId = videoComment.getParentId();
        userId = videoComment.getUserId();
        videoId = videoComment.getVideoId();
    }

}