package sk409.youtube.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import sk409.youtube.models.Playlist;

@Repository
public interface PlaylistRepository extends CrudRepository<Playlist, Long> {

}