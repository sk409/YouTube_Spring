package sk409.youtube.graph.builders;

import javax.persistence.EntityGraph;

import sk409.youtube.models.Video;
import sk409.youtube.models.Video_;

public class VideoGraphBuilder {

    public static final EntityGraphBuilder<Video> channel = EntityGraphBuilder.load(entityManager -> {
        final EntityGraph<Video> entityGraph = entityManager.createEntityGraph(Video.class);
        entityGraph.addAttributeNodes(Video_.CHANNEL);
        return entityGraph;
    });

    public static final EntityGraphBuilder<Video> rating = EntityGraphBuilder.load(entityManager -> {
        final EntityGraph<Video> entityGraph = entityManager.createEntityGraph(Video.class);
        entityGraph.addAttributeNodes(Video_.RATING);
        return entityGraph;
    });

    public static EntityGraphBuilder<Video> make(final String... nodes) {
        return EntityGraphBuilder.load(entityManager -> {
            final EntityGraph<Video> entityGraph = entityManager.createEntityGraph(Video.class);
            entityGraph.addAttributeNodes(nodes);
            return entityGraph;
        });
    }

}