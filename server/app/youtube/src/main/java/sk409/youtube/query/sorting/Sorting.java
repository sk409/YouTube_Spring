package sk409.youtube.query.sorting;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class Sorting<T> {

    @FunctionalInterface
    public interface SortFunction<U> {
        void apply(final Root<U> root, final CriteriaQuery<U> query, final CriteriaBuilder builder);
    }

    protected List<SortFunction<T>> sortFunctions = new ArrayList<>();

    public void sort(final Root<T> root, final CriteriaQuery<T> query, final CriteriaBuilder builder) {
        sortFunctions.forEach(sortFunction -> sortFunction.apply(root, query, builder));
    }

}