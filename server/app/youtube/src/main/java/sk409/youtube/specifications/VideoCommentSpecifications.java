package sk409.youtube.specifications;

import org.springframework.data.jpa.domain.Specification;

import lombok.Data;
import sk409.youtube.models.VideoComment;
import sk409.youtube.models.VideoComment_;

@Data
public class VideoCommentSpecifications implements Specifications<VideoComment> {
    private Long parentIdEqual;

    public Specification<VideoComment> equalToParentId() {
        return parentIdEqual == null ? null : (root, query, builder) -> {
            return builder.equal(root.get(VideoComment_.parentId), parentIdEqual);
        };
    }

    public Specification<VideoComment> where() {
        return Specification.where(equalToParentId());
    }
}