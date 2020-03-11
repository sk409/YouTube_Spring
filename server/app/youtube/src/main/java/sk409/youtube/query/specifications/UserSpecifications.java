package sk409.youtube.query.specifications;

import org.springframework.data.jpa.domain.Specification;

import lombok.Data;
import sk409.youtube.models.User;
import sk409.youtube.models.User_;

@Data
public class UserSpecifications implements Specifications<User> {

    private Long idEqual;
    private String usernameEqual;

    public Specification<User> where() {
        return Specification.where(equalToId()).and(equalToUsername());
    }

    private Specification<User> equalToId() {
        return idEqual == null ? null : (root, query, builder) -> {
            return builder.equal(root.get(User_.ID), idEqual);
        };
    }

    private Specification<User> equalToUsername() {
        return usernameEqual == null ? null : (root, query, builder) -> {
            return builder.equal(root.get(User_.USERNAME), usernameEqual);
        };
    }

}