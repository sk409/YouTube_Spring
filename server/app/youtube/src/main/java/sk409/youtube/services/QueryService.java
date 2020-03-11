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
import sk409.youtube.specifications.Specifications;

public abstract class QueryService<T> {

    private EntityManager entityManager;

    public QueryService(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    protected abstract Class<T> classLiteral();

    public List<T> findAll(final Specifications<T> specifications) {
        return findAll(specifications, null);
    }

    public List<T> findAll(final Specifications<T> specifications, final EntityGraphBuilder<T> entityGraphBuilder) {
        final List<T> list = makeTypedQuery(specifications, entityGraphBuilder).getResultList();
        return list;
    }

    public Optional<T> findOne(final Specifications<T> specifications) {
        return findOne(specifications, null);
    }

    public Optional<T> findOne(final Specifications<T> specifications, final EntityGraphBuilder<T> entityGraphBuilder) {
        final T obj = makeTypedQuery(specifications, entityGraphBuilder).setMaxResults(1).getSingleResult();
        return Optional.ofNullable(obj);
    }

    private TypedQuery<T> makeTypedQuery(final Specifications<T> specifications,
            final EntityGraphBuilder<T> entityGraphBuilder) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<T> query = builder.createQuery(classLiteral());
        final Root<T> root = query.from(classLiteral());
        query.select(root);
        final Specification<T> specification = specifications.where();
        final Predicate predicate = specification.toPredicate(root, query, builder);
        if (predicate != null) {
            query.where(predicate);
        }
        final TypedQuery<T> typedQuery = entityManager.createQuery(query);
        if (entityGraphBuilder != null) {
            final EntityGraph<T> entityGraph = entityGraphBuilder.build(entityManager);
            typedQuery.setHint(entityGraphBuilder.getType(), entityGraph);
        }
        return typedQuery;
    }

}