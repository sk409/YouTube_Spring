package sk409.youtube.query.specifications;

import org.springframework.data.jpa.domain.Specification;

public interface Specifications<T> {

    public void assign(final Specifications<T> other) throws IllegalArgumentException;

    public Specification<T> where();
}