package sk409.youtube.query.specifications;

import org.springframework.data.jpa.domain.Specification;

import lombok.Data;
import sk409.youtube.models.VideoRating;
import sk409.youtube.models.VideoRating_;

@Data
public class VideoRatingSpecifications implements Specifications<VideoRating> {
    private Long userIdEqual;
    private Long videoIdEqual;

    @Override
    public void assign(final Specifications<VideoRating> other) throws IllegalArgumentException {
        if (other == null) {
            return;
        }
        if (!(other instanceof VideoRatingSpecifications)) {
            throw new IllegalArgumentException();
        }
        final VideoRatingSpecifications videoRatingSpecifications = (VideoRatingSpecifications) other;
        if (userIdEqual == null) {
            userIdEqual = videoRatingSpecifications.getUserIdEqual();
        }
        if (videoIdEqual == null) {
            videoIdEqual = videoRatingSpecifications.getVideoIdEqual();
        }
    }

    @Override
    public Specification<VideoRating> where() {
        return Specification.where(equalToUserId()).and(equalToVideoId());
    }

    private Specification<VideoRating> equalToUserId() {
        return userIdEqual == null ? null : (root, query, builder) -> {
            return builder.equal(root.get(VideoRating_.USER_ID), userIdEqual);
        };
    }

    private Specification<VideoRating> equalToVideoId() {
        return videoIdEqual == null ? null : (root, query, builder) -> {
            return builder.equal(root.get(VideoRating_.VIDEO_ID), videoIdEqual);
        };
    }

}