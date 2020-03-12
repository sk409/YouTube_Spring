package sk409.youtube.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "video_comments")
public class VideoComment extends Model implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @Column(name = "video_id", nullable = false)
    @Getter
    @Setter
    private Long videoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false)
    @Getter
    @Setter
    private VideoComment parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false)
    @Getter
    @Setter
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false)
    @Getter
    @Setter
    private Video video;

    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    @Getter
    @Setter
    private List<VideoComment> children;

    @OneToMany(mappedBy = "videoComment")
    @Getter
    @Setter
    private List<VideoCommentRating> rating;

    public VideoComment() {
    }

    public VideoComment(final String text, final Long parentId, final Long userId, final Long videoId) {
        this.text = text;
        this.parentId = parentId;
        this.userId = userId;
        this.videoId = videoId;
    }

}