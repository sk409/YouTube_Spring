package sk409.youtube.query.specifications;

import org.springframework.data.jpa.domain.Specification;

public interface Specifications<T> {

    public Specification<T> where();
}