package sk409.youtube.graph.builders;

import javax.persistence.EntityGraph;

import sk409.youtube.models.Channel;
import sk409.youtube.models.Channel_;

public class ChannelGraphBuilder {

    public static final EntityGraphBuilder<Channel> user = EntityGraphBuilder.load(entityManager -> {
        final EntityGraph<Channel> channelGraph = entityManager.createEntityGraph(Channel.class);
        channelGraph.addAttributeNodes(Channel_.USER);
        return channelGraph;
    });

}