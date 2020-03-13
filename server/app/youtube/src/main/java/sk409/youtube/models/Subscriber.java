package sk409.youtube.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "subscribers")
public class Subscriber extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Column(name = "user_id", nullable = false)
    @Getter
    @Setter
    private Long userId;

    @Column(name = "channel_id", nullable = false)
    @Getter
    @Setter
    private Long channelId;

    @ManyToOne
    @JoinColumn(insertable = false, updatable = false)
    @Getter
    @Setter
    private User user;

    @ManyToOne
    @JoinColumn(insertable = false, updatable = false)
    @Getter
    @Setter
    private Channel channel;

    public Subscriber() {
    }

    public Subscriber(final Long userId, final Long channelId) {
        this.userId = userId;
        this.channelId = channelId;
    }

}