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
}