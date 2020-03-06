package sk409.youtube.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import sk409.youtube.models.Channel;
import sk409.youtube.models.User;
import sk409.youtube.repositories.ChannelRepository;

@Service
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final UserService userService;

    public ChannelService(ChannelRepository channelRepository, UserService userService) {
        this.channelRepository = channelRepository;
        this.userService = userService;
    }

    public Channel findById(Long id) {
        final Optional<Channel> _channel = channelRepository.findById(id);
        return _channel.isPresent() ? _channel.get() : null;
    }

    public List<Channel> findByUserId(Long userId) {
        final Optional<List<Channel>> _channels = channelRepository.findByUserId(userId);
        return _channels.isPresent() ? _channels.get() : null;
    }

    public Channel save(String name, Long userId) {
        final User user = userService.findById(userId);
        if (user == null) {
            return null;
        }
        final Channel channel = new Channel(name, user);
        channelRepository.save(channel);
        return channel;
    }

}