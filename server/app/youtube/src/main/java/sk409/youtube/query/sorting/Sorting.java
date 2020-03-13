package sk409.youtube.query.sorting;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public interface Sorting<T> {

    public void sort(final Root<T> root, final CriteriaQuery<T> query, final CriteriaBuilder builder);

}