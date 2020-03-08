package sk409.youtube.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import sk409.youtube.models.Channel;
import sk409.youtube.repositories.ChannelRepository;

@Service
public class ChannelService {

    private final ChannelRepository channelRepository;

    public ChannelService(final ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public Channel findById(final Long id) {
        final Optional<Channel> _channel = channelRepository.findById(id);
        return _channel.isPresent() ? _channel.get() : null;
    }

    public List<Channel> findByUserId(final Long userId) {
        final Optional<List<Channel>> _channels = channelRepository.findByUserId(userId);
        return _channels.isPresent() ? _channels.get() : null;
    }

    public Channel save(final String name, final Long userId) {
        final Channel channel = new Channel(name, userId);
        channelRepository.save(channel);
        return channel;
    }

}