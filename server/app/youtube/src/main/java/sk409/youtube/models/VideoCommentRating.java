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
@Table(name = "video_comment_rating")
public class VideoCommentRating extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column(name = "user_id", nullable = false)
    @Getter
    @Setter
    private Long userId;

    @Column(name = "video_comment_id", nullable = false)
    @Getter
    @Setter
    private Long videoCommentId;

    @Column(name = "rating_id", nullable = false)
    @Getter
    @Setter
    private Long ratingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false)
    @Getter
    @Setter
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false)
    @Getter
    @Setter
    private VideoComment videoComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false)
    @Getter
    @Setter
    private Rating rating;

    public VideoCommentRating() {
    }

    public VideoCommentRating(final Long userId, final Long videoCommentId, final Long ratingId) {
        this.userId = userId;
        this.videoCommentId = videoCommentId;
        this.ratingId = ratingId;
    }

}