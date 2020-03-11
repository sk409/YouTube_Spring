package sk409.youtube.graph.builders;

import java.util.function.Function;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EntityGraphBuilder<T> {

    private final EntityGraphType type;
    private final Function<EntityManager, EntityGraph<T>> mapper;

    public static <S> EntityGraphBuilder<S> fetch(final Function<EntityManager, EntityGraph<S>> mapper) {
        return new EntityGraphBuilder<>(EntityGraphType.FETCH, mapper);
    }

    public static <S> EntityGraphBuilder<S> load(final Function<EntityManager, EntityGraph<S>> mapper) {
        return new EntityGraphBuilder<>(EntityGraphType.LOAD, mapper);
    }

    public String getType() {
        return type.getKey();
    }

    public EntityGraph<T> build(final EntityManager em) {
        return mapper.apply(em);
    }
}
