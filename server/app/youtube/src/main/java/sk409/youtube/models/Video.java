package sk409.youtube.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "videos")
public class Video extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String overview;

	@Column(nullable = false)
	private Long views;

	@Column(nullable = false)
	private Float duration;

	@Column(nullable = false, unique = true)
	private String videoPath;

	@Column(nullable = false, unique = true)
	private String thumbnailPath;

	@Column(nullable = false, unique = true)
	private String uniqueId;

	@ManyToOne(fetch = FetchType.EAGER)
	private Channel channel;

	public Video() {
	}

	public Video(String title, String overview, Long views, Float duration, String videoPath, String thumbnailPath,
			String uniqueId, Channel channel) {
		this.title = title;
		this.overview = overview;
		this.views = views;
		this.duration = duration;
		this.videoPath = videoPath;
		this.thumbnailPath = thumbnailPath;
		this.uniqueId = uniqueId;
		this.channel = channel;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

}