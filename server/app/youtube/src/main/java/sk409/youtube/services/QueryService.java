package sk409.youtube.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import sk409.youtube.graph.builders.EntityGraphBuilder;
import sk409.youtube.query.QueryComponents;

public abstract class QueryService<T> {

    private EntityManager entityManager;

    public QueryService(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    protected abstract Class<T> classLiteral();

    public Optional<List<T>> findAll(final QueryComponents<T> queryComponents) {
        final List<T> list = makeTypedQuery(queryComponents).getResultList();
        return Optional.ofNullable(list.size() == 0 ? null : list);
    }

    public Optional<T> findOne(final QueryComponents<T> queryComponents) {
        return Optional.ofNullable(makeTypedQuery(queryComponents).setMaxResults(1).getSingleResult());
    }

    private TypedQuery<T> makeTypedQuery(final QueryComponents<T> queryComponents) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<T> query = builder.createQuery(classLiteral());
        final Root<T> root = query.from(classLiteral());
        query.select(root);
        if (queryComponents.getSpecifications() != null) {
            final Specification<T> specification = queryComponents.getSpecifications().where();
            final Predicate predicate = specification.toPredicate(root, query, builder);
            if (predicate != null) {
                query.where(predicate);
            }
        }
        final TypedQuery<T> typedQuery = entityManager.createQuery(query);
        if (queryComponents.getEntityGraphBuilder() != null) {
            final EntityGraphBuilder<T> entityGraphBuilder = queryComponents.getEntityGraphBuilder();
            final EntityGraph<T> entityGraph = entityGraphBuilder.build(entityManager);
            typedQuery.setHint(entityGraphBuilder.getType(), entityGraph);
        }
        return typedQuery;
    }

}