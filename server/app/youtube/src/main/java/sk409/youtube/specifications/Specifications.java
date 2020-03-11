package sk409.youtube.specifications;

import org.springframework.data.jpa.domain.Specification;

public interface Specifications<T> {

    public Specification<T> where();
}