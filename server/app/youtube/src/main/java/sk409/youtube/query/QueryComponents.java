package sk409.youtube.query;

import lombok.Data;
import sk409.youtube.graph.builders.EntityGraphBuilder;
import sk409.youtube.query.specifications.Specifications;

@Data
public class QueryComponents<T> {
    private EntityGraphBuilder<T> entityGraphBuilder;
    private Specifications<T> specifications;
    private Long limit;
}