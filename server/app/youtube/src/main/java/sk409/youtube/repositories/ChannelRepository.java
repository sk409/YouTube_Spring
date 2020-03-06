package sk409.youtube.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import sk409.youtube.models.Channel;

@Repository
public interface ChannelRepository extends CrudRepository<Channel, Long> {
    Optional<List<Channel>> findByUserId(Long userId);
}