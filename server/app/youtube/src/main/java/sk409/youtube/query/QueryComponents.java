package sk409.youtube.query;

import lombok.Data;
import sk409.youtube.graph.builders.EntityGraphBuilder;
import sk409.youtube.query.sorting.Sorting;
import sk409.youtube.query.specifications.Specifications;

@Data
public class QueryComponents<T> {
    private EntityGraphBuilder<T> entityGraphBuilder;
    private Specifications<T> specifications;
    private Sorting<T> sorting;
    private Integer limit;

    public void assign(final QueryComponents<T> other) {
        if (entityGraphBuilder == null) {
            entityGraphBuilder = other.getEntityGraphBuilder();
        }
        if (specifications == null) {
            specifications = other.getSpecifications();
        } else {
            specifications.assign(other.getSpecifications());
        }
        if (sorting == null) {
            sorting = other.getSorting();
        }
        if (limit == null) {
            limit = other.getLimit();
        }
    }
}