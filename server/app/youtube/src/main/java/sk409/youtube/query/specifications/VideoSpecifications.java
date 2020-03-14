package sk409.youtube.query.specifications;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import lombok.Data;
import sk409.youtube.models.Video;
import sk409.youtube.models.Video_;

@Data
public class VideoSpecifications implements Specifications<Video> {

    private List<Long> idNotIn;
    private Long channelIdEqual;
    private String uniqueIdEqual;

    public Specification<Video> where() {
        return Specification.where(equalToChannelId()).and(equalToUniqueId()).and(notInId());
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

    private Specification<Video> notInId() {
        return idNotIn == null ? null : (root, query, builder) -> {
            return builder.not(root.get(Video_.ID).in(idNotIn));
        };
    }
}