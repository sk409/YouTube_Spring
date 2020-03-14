package sk409.youtube.models;

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

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "channels")
public class Channel extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column(length = 256, nullable = false)
    @Getter
    @Setter
    private String name;

    @Column(nullable = false)
    @Getter
    @Setter
    private String profileImagePath;

    @Column(name = "user_id", nullable = false)
    @Getter
    @Setter
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false)
    @Getter
    @Setter
    private User user;

    @OneToMany(mappedBy = "channel")
    @Getter
    @Setter
    private List<Video> videos;

    public Channel() {
    }

    public Channel(final String name, final String profileImagePath, final Long userId) {
        this.name = name;
        this.profileImagePath = profileImagePath;
        this.userId = userId;
    }

}