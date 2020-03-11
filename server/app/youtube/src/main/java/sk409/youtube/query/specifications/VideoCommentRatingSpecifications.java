package sk409.youtube.query.specifications;

import org.springframework.data.jpa.domain.Specification;

import lombok.Data;
import sk409.youtube.models.VideoCommentRating;
import sk409.youtube.models.VideoCommentRating_;

@Data
public class VideoCommentRatingSpecifications implements Specifications<VideoCommentRating> {

    private Long userIdEqual;
    private Long videoCommentIdEqual;

    public Specification<VideoCommentRating> where() {
        return Specification.where(equalToUserId()).and(equalToVideoCommentId());
    }

    private Specification<VideoCommentRating> equalToUserId() {
        return userIdEqual == null ? null : (root, query, builder) -> {
            return builder.equal(root.get(VideoCommentRating_.USER_ID), userIdEqual);
        };
    }

    private Specification<VideoCommentRating> equalToVideoCommentId() {
        return videoCommentIdEqual == null ? null : (root, query, builder) -> {
            return builder.equal(root.get(VideoCommentRating_.VIDEO_COMMENT_ID), videoCommentIdEqual);
        };
    }

}