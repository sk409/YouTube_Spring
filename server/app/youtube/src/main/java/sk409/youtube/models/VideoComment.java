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
@Table(name="video_comments")
public class VideoComment extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column(nullable = false)
    @Getter
    @Setter
    private String text;

    @Column(name = "parent_id")
    @Getter
    @Setter
    private Long parentId;

    @Column(name = "user_id")
    @Getter
    @Setter
    private Long userId;

    @Column(name="video_id", nullable = false)
    @Getter
    @Setter
    private Long videoId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(insertable = false, updatable = false)
    @Getter
    @Setter
    private VideoComment parent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(insertable = false, updatable = false)
    @Getter
    @Setter
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(insertable = false, updatable = false)
    @Getter
    @Setter
    private Video video;

    public VideoComment() {}

    public VideoComment(final String text, final Long parentId, final Long userId, final Long videoId) {
        this.text = text;
        this.parentId = parentId;
        this.userId = userId;
        this.videoId = videoId;
    }    

}