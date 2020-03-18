package sk409.youtube.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "playlists")
public class Playlist extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column(nullable = false)
    @Getter
    @Setter
    private String name;

    @Column(nullable = false)
    @Getter
    @Setter
    private String overview;

    @Column(name = "channel_id", nullable = false)
    @Getter
    @Setter
    private Long channelId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false)
    @Getter
    @Setter
    private Channel channel;

    public Playlist() {
    }

    public Playlist(final String name, final String overview, final Long channelId) {
        this.name = name;
        this.overview = overview;
        this.channelId = channelId;
    }

}