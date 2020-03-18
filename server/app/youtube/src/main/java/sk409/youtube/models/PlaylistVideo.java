package sk409.youtube.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Embeddable
class PlaylistVideoKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "playlist_id", nullable = false)
    @Getter
    private Long playlistId;

    @Column(name = "video_id", nullable = false)
    @Getter
    private Long videoId;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((playlistId == null) ? 0 : playlistId.hashCode());
        result = prime * result + ((videoId == null) ? 0 : videoId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PlaylistVideoKey other = (PlaylistVideoKey) obj;
        if (playlistId == null) {
            if (other.playlistId != null)
                return false;
        } else if (!playlistId.equals(other.playlistId))
            return false;
        if (videoId == null) {
            if (other.videoId != null)
                return false;
        } else if (!videoId.equals(other.videoId))
            return false;
        return true;
    }

}

@Entity
@Table(name = "playlist_video")
@IdClass(PlaylistVideoKey.class)
public class PlaylistVideo {

    @Id
    @Column(name = "playlist_id", nullable = false)
    @Getter
    private Long playlistId;

    @Id
    @Column(name = "video_id", nullable = false)
    @Getter
    private Long videoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("playlist_id")
    @JoinColumn(insertable = false, updatable = false)
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("video_id")
    @JoinColumn(insertable = false, updatable = false)
    private Video video;

    public PlaylistVideo() {
    }

    public PlaylistVideo(final Long playlistId, final Long videoId) {
        this.playlistId = playlistId;
        this.videoId = videoId;
    }

}