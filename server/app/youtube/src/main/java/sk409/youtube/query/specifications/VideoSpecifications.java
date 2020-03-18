package sk409.youtube.query.specifications;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import lombok.Data;
import sk409.youtube.models.Video;
import sk409.youtube.models.Video_;

@Data
public class VideoSpecifications implements Specifications<Video> {

    private Long idGreaterThan;
    private Long idLessThan;
    private List<Long> idNotIn;
    private Long channelIdEqual;
    private String uniqueIdEqual;

    @Override
    public void assign(final Specifications<Video> other) throws IllegalArgumentException {
        if (other == null) {
            return;
        }
        if (!(other instanceof VideoSpecifications)) {
            throw new IllegalArgumentException();
        }
        final VideoSpecifications videoSpecifications = (VideoSpecifications) other;
        if (idGreaterThan == null) {
            idGreaterThan = videoSpecifications.getIdGreaterThan();
        }
        if (idLessThan == null) {
            idLessThan = videoSpecifications.getIdLessThan();
        }
        if (idNotIn == null) {
            idNotIn = videoSpecifications.getIdNotIn();
        }
        if (channelIdEqual == null) {
            channelIdEqual = videoSpecifications.getChannelIdEqual();
        }
        if (uniqueIdEqual == null) {
            uniqueIdEqual = videoSpecifications.getUniqueIdEqual();
        }
    }

    @Override
    public Specification<Video> where() {
        return Specification.where(equalToChannelId()).and(equalToUniqueId()).and(greaterThanId()).and(lessThanId())
                .and(notInId());
    }

    private Specification<Video> equalToChannelId() {
        return channelIdEqual == null ? null : (root, query, builder) -> {
            return builder.equal(root.get(Video_.CHANNEL_ID), channelIdEqual);
        };
    }

    private Specification<Video> equalToUniqueId() {
        return uniqueIdEqual == null ? null : (root, query, builder) -> {
            return builder.equal(root.get(Video_.UNIQUE_ID), uniqueIdEqual);
        };
    }

    private Specification<Video> greaterThanId() {
        return idGreaterThan == null ? null : (root, query, builder) -> {
            return builder.greaterThan(root.get(Video_.ID), idGreaterThan);
        };
    }

    private Specification<Video> lessThanId() {
        return idLessThan == null ? null : (root, query, builder) -> {
            return builder.lessThan(root.get(Video_.ID), idLessThan);
        };
    }

    private Specification<Video> notInId() {
        return idNotIn == null ? null : (root, query, builder) -> {
            return builder.not(root.get(Video_.ID).in(idNotIn));
        };
    }
}