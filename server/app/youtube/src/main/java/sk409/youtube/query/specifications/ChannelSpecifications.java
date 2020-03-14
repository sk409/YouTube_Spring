package sk409.youtube.query.specifications;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import lombok.Data;
import sk409.youtube.models.Channel;
import sk409.youtube.models.Channel_;

@Data
public class ChannelSpecifications implements Specifications<Channel> {
    private Long idEqual;
    private List<Long> idIn;
    private List<Long> idNotIn;
    private Long userIdEqual;

    public Specification<Channel> where() {
        return Specification.where(equalToId()).and(equalToUserId()).and(inId()).and(notInId());
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

    private Specification<Channel> inId() {
        return idIn == null ? null : (root, query, builder) -> {
            return root.get(Channel_.ID).in(idIn);
        };
    }

    private Specification<Channel> notInId() {
        return idNotIn == null ? null : (root, query, builder) -> {
            return builder.not(root.get(Channel_.ID).in(idNotIn));
        };
    }
}