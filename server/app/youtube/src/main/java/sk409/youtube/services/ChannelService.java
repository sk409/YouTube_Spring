package sk409.youtube.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;

import sk409.youtube.models.Channel;
import sk409.youtube.models.Subscriber;
import sk409.youtube.query.QueryComponents;
import sk409.youtube.query.specifications.ChannelSpecifications;
import sk409.youtube.repositories.ChannelRepository;

@Service
public class ChannelService extends QueryService<Channel> {

    private final ChannelRepository channelRepository;
    private final SubscriberService subscriberService;

    public ChannelService(final ChannelRepository channelRepository, final EntityManager entityManager,
            final SubscriberService subscriberService) {
        super(entityManager);
        this.channelRepository = channelRepository;
        this.subscriberService = subscriberService;
    }

    @Override
    public Class<Channel> classLiteral() {
        return Channel.class;
    }

    public Optional<List<Channel>> findSubscription(final Long userId) {
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
        final Optional<List<Channel>> _channels = findAll(channelQueryComponents);
        return _channels;
    }

    public Channel save(final String name, final Long userId) {
        final Channel channel = new Channel(name, userId);
        channelRepository.save(channel);
        return channel;
    }

}