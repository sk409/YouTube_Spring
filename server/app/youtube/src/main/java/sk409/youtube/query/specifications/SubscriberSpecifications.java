package sk409.youtube.query.specifications;

import org.springframework.data.jpa.domain.Specification;

import lombok.Data;
import sk409.youtube.models.Subscriber;
import sk409.youtube.models.Subscriber_;

@Data
public class SubscriberSpecifications implements Specifications<Subscriber> {

    private Long channelIdEqual;
    private Long idEqual;
    private Long userIdEqual;

    public Specification<Subscriber> where() {
        return Specification.where(equalToChannelId()).and(equalToId()).and(equalToUserId());
    }

    private Specification<Subscriber> equalToChannelId() {
        return channelIdEqual == null ? null : (root, query, builder) -> {
            return builder.equal(root.get(Subscriber_.CHANNEL_ID), channelIdEqual);
        };
    }

    private Specification<Subscriber> equalToId() {
        return idEqual == null ? null : (root, query, builder) -> {
            return builder.equal(root.get(Subscriber_.ID), idEqual);
        };
    }

    private Specification<Subscriber> equalToUserId() {
        return userIdEqual == null ? null : (root, query, builder) -> {
            return builder.equal(root.get(Subscriber_.USER_ID), userIdEqual);
        };
    }
}