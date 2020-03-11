package sk409.youtube.query.specifications;

import org.springframework.data.jpa.domain.Specification;

import lombok.Data;
import sk409.youtube.models.VideoComment;
import sk409.youtube.models.VideoComment_;

@Data
public class VideoCommentSpecifications implements Specifications<VideoComment> {
    private Long parentIdEqual;
    private Long videoIdEqual;
    private Long[] videoIdIn;

    public Specification<VideoComment> where() {
        return Specification.where(equalToParentId()).and(equalToVideoId()).and(inVideoId());
    }

    private Specification<VideoComment> equalToParentId() {
        return parentIdEqual == null ? null : (root, query, builder) -> {
            return builder.equal(root.get(VideoComment_.PARENT_ID), parentIdEqual);
        };
    }

    private Specification<VideoComment> equalToVideoId() {
        return videoIdEqual == null ? null : (root, query, builder) -> {
            return builder.equal(root.get(VideoComment_.VIDEO_ID), videoIdEqual);
        };
    }

    private Specification<VideoComment> inVideoId() {
        return videoIdIn == null ? null : (root, query, builder) -> {
            return root.get(VideoComment_.VIDEO_ID).in((Object[]) videoIdIn);
        };
    }
}