package sk409.youtube.query.specifications;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import lombok.Data;
import sk409.youtube.models.VideoComment;
import sk409.youtube.models.VideoComment_;

@Data
public class VideoCommentSpecifications implements Specifications<VideoComment> {
    private List<Long> idIn;
    private Long idLessThan;
    private Long idGreaterThan;
    private List<Long> idNotIn;
    private Long parentIdEqual;
    private Boolean parentIdIsNull;
    private Long videoIdEqual;
    private List<Long> videoIdIn;

    @Override
    public void assign(final Specifications<VideoComment> other) throws IllegalArgumentException {
        if (other == null) {
            return;
        }
        if (!(other instanceof VideoCommentSpecifications)) {
            throw new IllegalArgumentException();
        }
        final VideoCommentSpecifications videoCommentSpecifications = (VideoCommentSpecifications) other;
        if (idIn == null) {
            idIn = videoCommentSpecifications.getIdIn();
        }
        if (idLessThan == null) {
            idLessThan = videoCommentSpecifications.getIdLessThan();
        }
        if (idGreaterThan == null) {
            idGreaterThan = videoCommentSpecifications.getIdGreaterThan();
        }
        if (idNotIn == null) {
            idNotIn = videoCommentSpecifications.getIdNotIn();
        }
        if (parentIdEqual == null) {
            parentIdEqual = videoCommentSpecifications.getParentIdEqual();
        }
        if (parentIdIsNull == null) {
            parentIdIsNull = videoCommentSpecifications.getParentIdIsNull();
        }
        if (videoIdEqual == null) {
            videoIdEqual = videoCommentSpecifications.getVideoIdEqual();
        }
        if (videoIdIn == null) {
            videoIdIn = videoCommentSpecifications.getVideoIdIn();
        }
    }

    @Override
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
            return root.get(VideoComment_.ID).in(idIn);
        };
    }

    private Specification<VideoComment> inVideoId() {
        return videoIdIn == null ? null : (root, query, builder) -> {
            return root.get(VideoComment_.VIDEO_ID).in(videoIdIn);
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
            return root.get(VideoComment_.ID).in(idNotIn).not();
        };
    }
}