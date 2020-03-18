package sk409.youtube.query.specifications;

import org.springframework.data.jpa.domain.Specification;

import lombok.Data;
import sk409.youtube.models.Playlist;
import sk409.youtube.models.Playlist_;

@Data
public class PlaylistSpecifications implements Specifications<Playlist> {

    private Long channelIdEqual;

    @Override
    public void assign(final Specifications<Playlist> other) throws IllegalArgumentException {
        if (other == null) {
            return;
        }
        if (!(other instanceof PlaylistSpecifications)) {
            throw new IllegalArgumentException();
        }
        final PlaylistSpecifications playlistSpecifications = (PlaylistSpecifications) other;
        if (channelIdEqual == null) {
            channelIdEqual = playlistSpecifications.getChannelIdEqual();
        }
    }

    @Override
    public Specification<Playlist> where() {
        return Specification.where(equalChannelId());
    }

    private Specification<Playlist> equalChannelId() {
        return channelIdEqual == null ? null : (root, query, builder) -> {
            return builder.equal(root.get(Playlist_.CHANNEL_ID), channelIdEqual);
        };
    }

}