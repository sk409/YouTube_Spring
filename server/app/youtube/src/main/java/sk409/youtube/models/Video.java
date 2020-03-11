package sk409.youtube.models;

import java.util.List;
import java.util.Set;

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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "videos")
public class Video extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	private Long id;

	@Column(length = 100, nullable = false)
	@Getter
	@Setter
	private String title;

	@Column(length = 5000, nullable = false)
	@Getter
	@Setter
	private String overview;

	@Column(nullable = false)
	@Getter
	@Setter
	private Long views;

	@Column(nullable = false)
	@Getter
	@Setter
	private Float duration;

	@Column(length = 256, nullable = false, unique = true)
	@Getter
	@Setter
	private String videoPath;

	@Column(length = 256, nullable = false, unique = true)
	@Getter
	@Setter
	private String thumbnailPath;

	@Column(length = 256, nullable = false, unique = true)
	@Getter
	@Setter
	private String uniqueId;

	@Column(name = "channel_id", nullable = false)
	@Getter
	@Setter
	private Long channelId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(insertable = false, updatable = false)
	@Getter
	@Setter
	private Channel channel;

	@OneToMany(mappedBy = "video")
	@Fetch(value = FetchMode.SUBSELECT)
	@Getter
	@Setter
	private List<VideoComment> comments;

	@OneToMany(mappedBy = "video")
	@Fetch(value = FetchMode.SUBSELECT)
	@Getter
	@Setter
	private List<VideoRating> rating;

	public Video() {
	}

	public Video(final String title, final String overview, final Long views, final Float duration,
			final String videoPath, final String thumbnailPath, final String uniqueId, final Long channelId) {
		this.title = title;
		this.overview = overview;
		this.views = views;
		this.duration = duration;
		this.videoPath = videoPath;
		this.thumbnailPath = thumbnailPath;
		this.uniqueId = uniqueId;
		this.channelId = channelId;
	}
}