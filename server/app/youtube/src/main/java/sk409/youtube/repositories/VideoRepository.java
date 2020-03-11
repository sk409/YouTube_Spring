package sk409.youtube.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import sk409.youtube.models.Video;

@Repository
public interface VideoRepository extends CrudRepository<Video, Long> {
    Optional<List<Video>> findByChannelId(Long channelId);

    Optional<Video> findByUniqueId(String uniqueId);
}