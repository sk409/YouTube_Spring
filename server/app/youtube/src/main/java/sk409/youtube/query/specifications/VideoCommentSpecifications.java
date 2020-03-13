package sk409.youtube.query.specifications;

import org.springframework.data.jpa.domain.Specification;

import lombok.Data;
import sk409.youtube.models.VideoComment;
import sk409.youtube.models.VideoComment_;

@Data
public class VideoCommentSpecifications implements Specifications<VideoComment> {
    private Long[] idIn;
    private Long idLessThan;
    private Long idGreaterThan;
    private Long[] idNotIn;
    private Long parentIdEqual;
    private Boolean parentIdIsNull;
    private Long videoIdEqual;
    private Long[] videoIdIn;

    public Specification<VideoComment> where() {
        return Specification.where(equalToParentId()).and(equalToVideoId()).and(greaterThanId()).and(inId())
                .and(inVideoId()).and(isNullParentId()).and(lessThanId()).and(notInId());
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

    private Specification<VideoComment> greaterThanId() {
        return idGreaterThan == null ? null : (root, query, builder) -> {
            return builder.greaterThan(root.get(VideoComment_.ID), idGreaterThan);
        };
    }

    private Specification<VideoComment> inId() {
        return idIn == null ? null : (root, query, builder) -> {
            return root.get(VideoComment_.ID).in((Object[]) idIn);
        };
    }

    private Specification<VideoComment> inVideoId() {
        return videoIdIn == null ? null : (root, query, builder) -> {
            return root.get(VideoComment_.VIDEO_ID).in((Object[]) videoIdIn);
        };
    }

    private Specification<VideoComment> isNullParentId() {
        return parentIdIsNull == null || !parentIdIsNull ? null : (root, query, builder) -> {
            return root.get(VideoComment_.PARENT_ID).isNull();
        };
    }

    private Specification<VideoComment> lessThanId() {
        return idLessThan == null ? null : (root, query, builder) -> {
            return builder.lessThan(root.get(VideoComment_.ID), idLessThan);
        };
    }

    private Specification<VideoComment> notInId() {
        return idNotIn == null ? null : (root, query, builder) -> {
            return root.get(VideoComment_.ID).in((Object[]) idNotIn).not();
        };
    }
}