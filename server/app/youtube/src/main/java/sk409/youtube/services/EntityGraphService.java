package sk409.youtube.services;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
//import javax.persistence.Subgraph;

// import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.stereotype.Service;

@Service
public class EntityGraphService {

    public <T> EntityGraph<T> build(final EntityManager entityManager, final Class<T> rootType,
            final String... fields) {
        final EntityGraph<T> entityGraph = entityManager.createEntityGraph(rootType);
        // EntityGraphType.FETCH
        // Subgraph<T> sub;
        return entityGraph;
    }

}