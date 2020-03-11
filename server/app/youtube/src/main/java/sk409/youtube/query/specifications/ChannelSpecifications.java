package sk409.youtube.query.specifications;

import org.springframework.data.jpa.domain.Specification;

import lombok.Data;
import sk409.youtube.models.Channel;
import sk409.youtube.models.Channel_;

@Data
public class ChannelSpecifications implements Specifications<Channel> {
    private Long idEqual;
    private Long userIdEqual;

    public Specification<Channel> where() {
        return Specification.where(equalToId()).and(equalToUserId());
    }

    private Specification<Channel> equalToId() {
        return idEqual == null ? null : (root, query, builder) -> {
            return builder.equal(root.get(Channel_.ID), idEqual);
        };
    }

    private Specification<Channel> equalToUserId() {
        return userIdEqual == null ? null : (root, query, builder) -> {
            return builder.equal(root.get(Channel_.USER_ID), userIdEqual);
        };
    }
}