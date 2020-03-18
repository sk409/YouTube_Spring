package sk409.youtube.query.specifications;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import lombok.Data;
import sk409.youtube.models.Subscriber;
import sk409.youtube.models.Subscriber_;

@Data
public class SubscriberSpecifications implements Specifications<Subscriber> {

    private Long channelIdEqual;
    private List<Long> channelIdNotIn;
    private Long idEqual;
    private List<Long> idNotIn;
    private Long userIdEqual;

    @Override
    public void assign(final Specifications<Subscriber> other) throws IllegalArgumentException {
        if (other == null) {
            return;
        }
        if (!(other instanceof SubscriberSpecifications)) {
            throw new IllegalArgumentException();
        }
        final SubscriberSpecifications subscriberSpecifications = (SubscriberSpecifications) other;
        if (channelIdEqual == null) {
            channelIdEqual = subscriberSpecifications.getChannelIdEqual();
        }
        if (channelIdNotIn == null) {
            channelIdNotIn = subscriberSpecifications.getChannelIdNotIn();
        }
        if (idEqual == null) {
            idEqual = subscriberSpecifications.getIdEqual();
        }
        if (idNotIn == null) {
            idNotIn = subscriberSpecifications.getIdNotIn();
        }
        if (userIdEqual == null) {
            userIdEqual = subscriberSpecifications.getUserIdEqual();
        }
    }

    @Override
    public Specification<Subscriber> where() {
        return Specification.where(equalToChannelId()).and(equalToId()).and(equalToUserId()).and(notInChannelId())
                .and(notInId());
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

    private Specification<Subscriber> notInChannelId() {
        return channelIdNotIn == null ? null : (root, query, builder) -> {
            return builder.not(root.get(Subscriber_.CHANNEL_ID).in(channelIdNotIn));
        };
    }

    private Specification<Subscriber> notInId() {
        return idNotIn == null ? null : (root, query, builder) -> {
            return builder.not(root.get(Subscriber_.ID).in(idNotIn));
        };
    }
}