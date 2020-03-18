package sk409.youtube.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;

import sk409.youtube.graph.builders.EntityGraphBuilder;
import sk409.youtube.models.Channel;
import sk409.youtube.models.Subscriber;
import sk409.youtube.query.QueryComponents;
import sk409.youtube.query.specifications.ChannelSpecifications;
import sk409.youtube.repositories.ChannelRepository;

@Service
public class ChannelService extends QueryService<Channel> {

    private final ChannelRepository channelRepository;
    private final PathService pathService;
    private final SubscriberService subscriberService;

    public ChannelService(final ChannelRepository channelRepository, final EntityManager entityManager,
            final PathService pathService, final SubscriberService subscriberService) {
        super(entityManager);
        this.channelRepository = channelRepository;
        this.pathService = pathService;
        this.subscriberService = subscriberService;
    }

    @Override
    public Class<Channel> classLiteral() {
        return Channel.class;
    }

    public Optional<List<Channel>> findByIdIn(final List<Long> ids) {
        final ChannelSpecifications channelSpecifications = new ChannelSpecifications();
        channelSpecifications.setIdIn(ids);
        final QueryComponents<Channel> channelQueryComponents = new QueryComponents<>();
        channelQueryComponents.setSpecifications(channelSpecifications);
        final Optional<List<Channel>> _channels = findAll(channelQueryComponents);
        return _channels;
    }

    public Optional<List<Channel>> findByIdNotIn(final List<Long> ids) {
        final ChannelSpecifications channelSpecifications = new ChannelSpecifications();
        channelSpecifications.setIdNotIn(ids);
        final QueryComponents<Channel> channelQueryComponents = new QueryComponents<>();
        channelQueryComponents.setSpecifications(channelSpecifications);
        final Optional<List<Channel>> _channels = findAll(channelQueryComponents);
        return _channels;
    }

    public Optional<Channel> findByUniqueId(final String uniqueId) {
        return findByUniqueId(uniqueId, (QueryComponents<Channel>) null);
    }

    public Optional<Channel> findByUniqueId(final String uniqueId,
            final EntityGraphBuilder<Channel> channelGraphBuilder) {
        final QueryComponents<Channel> options = new QueryComponents<Channel>();
        options.setEntityGraphBuilder(channelGraphBuilder);
        return findByUniqueId(uniqueId, options);
    }

    public Optional<Channel> findByUniqueId(final String uniqueId, final QueryComponents<Channel> options) {
        final ChannelSpecifications channelSpecifications = new ChannelSpecifications();
        channelSpecifications.setUniqueIdEqual(uniqueId);
        final QueryComponents<Channel> channelQueryComponents = new QueryComponents<>();
        channelQueryComponents.setSpecifications(channelSpecifications);
        if (options != null) {
            channelQueryComponents.assign(options);
        }
        final Optional<Channel> _channel = findOne(channelQueryComponents);
        return _channel;
    }

    public Optional<Channel> findByUserId(final Long userId) {
        final ChannelSpecifications channelSpecifications = new ChannelSpecifications();
        channelSpecifications.setUserIdEqual(userId);
        final QueryComponents<Channel> channelQueryComponents = new QueryComponents<>();
        channelQueryComponents.setSpecifications(channelSpecifications);
        final Optional<Channel> _channel = findOne(channelQueryComponents);
        return _channel;
    }

    public Optional<List<Channel>> findSubscription(final Long userId) {
        return findSubscription(userId, null);
    }

    public Optional<List<Channel>> findSubscription(final Long userId, final QueryComponents<Channel> options) {
        final Optional<List<Subscriber>> _subscribers = subscriberService.findByUserId(userId);
        if (!_subscribers.isPresent()) {
            return Optional.ofNullable(null);
        }
        final List<Subscriber> subscribers = _subscribers.get();
        final List<Long> channelIds = subscribers.stream().map(subscriber -> subscriber.getChannelId())
                .collect(Collectors.toList());
        final ChannelSpecifications channelSpecifications = new ChannelSpecifications();
        channelSpecifications.setIdIn(channelIds);
        final QueryComponents<Channel> channelQueryComponents = new QueryComponents<>();
        channelQueryComponents.setSpecifications(channelSpecifications);
        if (options != null) {
            channelQueryComponents.assign(options);
        }
        final Optional<List<Channel>> _channels = findAll(channelQueryComponents);
        return _channels;
    }

    public Channel save(final String name, final String uniqueId, final Long userId) {
        return save(name, pathService.getRelativeNoImagePath().toString(), uniqueId, userId);
    }

    public Channel save(final String name, final String profileImagePath, final String uniqueId, final Long userId) {
        final Channel channel = new Channel(name, profileImagePath, uniqueId, userId);
        channelRepository.save(channel);
        return channel;
    }

}