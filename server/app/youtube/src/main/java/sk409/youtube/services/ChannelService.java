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

    public Optional<Channel> findById(final Long id) {
        return channelRepository.findById(id);
    }

    public Optional<List<Channel>> findByUserId(final Long userId) {
        return channelRepository.findByUserId(userId);
    }

    public Channel save(final String name, final Long userId) {
        final Channel channel = new Channel(name, userId);
        channelRepository.save(channel);
        return channel;
    }

}