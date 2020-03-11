package sk409.youtube.specifications;

import org.springframework.data.jpa.domain.Specification;

import lombok.Data;
import sk409.youtube.models.Video;
import sk409.youtube.models.Video_;

@Data
public class VideoSpecifications implements Specifications<Video> {
    private Long channelIdEqual;

    public Specification<Video> equalToChannelId() {
        return channelIdEqual == null ? null : (root, query, builder) -> {
            return builder.equal(root.get(Video_.channelId), channelIdEqual);
        };
    }

    public Specification<Video> where() {
        return Specification.where(equalToChannelId());
    }
}