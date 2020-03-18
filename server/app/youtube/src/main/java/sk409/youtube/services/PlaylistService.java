package sk409.youtube.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;

import sk409.youtube.models.Playlist;
import sk409.youtube.query.QueryComponents;
import sk409.youtube.query.specifications.PlaylistSpecifications;
import sk409.youtube.repositories.PlaylistRepository;

@Service
public class PlaylistService extends QueryService<Playlist> {

    private final PlaylistRepository playlistRepository;

    public PlaylistService(final EntityManager entityManager, final PlaylistRepository playlistRepository) {
        super(entityManager);
        this.playlistRepository = playlistRepository;
    }

    @Override
    public Class<Playlist> classLiteral() {
        return Playlist.class;
    }

    public Optional<List<Playlist>> findByChannelId(final Long channelId) {
        final PlaylistSpecifications playlistSpecifications = new PlaylistSpecifications();
        playlistSpecifications.setChannelIdEqual(channelId);
        final QueryComponents<Playlist> playlistQueryComponents = new QueryComponents<>();
        playlistQueryComponents.setSpecifications(playlistSpecifications);
        final Optional<List<Playlist>> _playlists = findAll(playlistQueryComponents);
        return _playlists;
    }

    public Playlist save(final String name, final String overview, final Long channelId) {
        final Playlist playlist = new Playlist(name, overview, channelId);
        playlistRepository.save(playlist);
        return playlist;
    }

}