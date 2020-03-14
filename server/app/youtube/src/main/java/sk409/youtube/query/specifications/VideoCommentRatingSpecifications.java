package sk409.youtube.query.specifications;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import lombok.Data;
import sk409.youtube.models.VideoCommentRating;
import sk409.youtube.models.VideoCommentRating_;

@Data
public class VideoCommentRatingSpecifications implements Specifications<VideoCommentRating> {

    private Long idEqual;
    private Long userIdEqual;
    private Long userIdNotEqual;
    private Long videoCommentIdEqual;
    private List<Long> videoCommentIdIn;

    public Specification<VideoCommentRating> where() {
        return Specification.where(equalToId()).and(equalToUserId()).and(equalToVideoCommentId())
                .and(inVideoCommentId()).and(notEqualToUserId());
    }

    private Specification<VideoCommentRating> equalToId() {
        return idEqual == null ? null : (root, query, builder) -> {
            return builder.equal(root.get(VideoCommentRating_.ID), idEqual);
        };
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

    private Specification<VideoCommentRating> inVideoCommentId() {
        return videoCommentIdIn == null ? null : (root, query, builder) -> {
            return root.get(VideoCommentRating_.VIDEO_COMMENT_ID).in(videoCommentIdIn);
        };
    }

    private Specification<VideoCommentRating> notEqualToUserId() {
        return userIdNotEqual == null ? null : (root, query, builder) -> {
            return builder.notEqual(root.get(VideoCommentRating_.USER_ID), userIdNotEqual);
        };
    }

}