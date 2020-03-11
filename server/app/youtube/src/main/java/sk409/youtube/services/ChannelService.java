package sk409.youtube.services;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;

import sk409.youtube.models.Channel;
import sk409.youtube.repositories.ChannelRepository;

@Service
public class ChannelService extends QueryService<Channel> {

    private final ChannelRepository channelRepository;

    public ChannelService(final ChannelRepository channelRepository, final EntityManager entityManager) {
        super(entityManager);
        this.channelRepository = channelRepository;
    }

    @Override
    public Class<Channel> classLiteral() {
        return Channel.class;
    }

    // public Optional<Channel> findById(final Long id) {
    // return channelRepository.findById(id);
    // }

    // public Optional<List<Channel>> findByUserId(final Long userId) {
    // return channelRepository.findByUserId(userId);
    // }

    public Channel save(final String name, final Long userId) {
        final Channel channel = new Channel(name, userId);
        channelRepository.save(channel);
        return channel;
    }

}