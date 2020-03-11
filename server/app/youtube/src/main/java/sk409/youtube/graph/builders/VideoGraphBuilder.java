package sk409.youtube.graph.builders;

import javax.persistence.EntityGraph;

import sk409.youtube.models.Video;
import sk409.youtube.models.Video_;

public class VideoGraphBuilder {

    public static final EntityGraphBuilder<Video> reaction = EntityGraphBuilder.load(entityManager -> {
        final EntityGraph<Video> entityGraph = entityManager.createEntityGraph(Video.class);
        entityGraph.addAttributeNodes(Video_.COMMENTS, Video_.RATING);
        return entityGraph;
    });

}