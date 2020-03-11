package sk409.youtube.graph.builders;

import javax.persistence.EntityGraph;

import sk409.youtube.models.VideoComment;
import sk409.youtube.models.VideoComment_;

public class VideoCommentGraphBuilder {

    public static final EntityGraphBuilder<VideoComment> watch = EntityGraphBuilder.load(entityManager -> {
        final EntityGraph<VideoComment> entityGraph = entityManager.createEntityGraph(VideoComment.class);
        entityGraph.addAttributeNodes(VideoComment_.USER);
        entityGraph.addAttributeNodes(VideoComment_.CHILDREN);
        return entityGraph;
    });

}