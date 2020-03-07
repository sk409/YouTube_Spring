package sk409.youtube.requests;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

public class VideoStoreRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Size(min = 1, max = 100)
	private String title;

	@Size(max = 5000)
	private String overview;

	@NotNull
	private Float duration;

	@NotNull
	@Size(min = 1, max = 255)
	private String uniqueId;

	@NotNull
	private MultipartFile thumbnail;

	@NotNull
	private MultipartFile video;

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

	public Float getDuration() {
		return duration;
	}

	public void setDuration(Float duration) {
		this.duration = duration;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public MultipartFile getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(MultipartFile thumbnail) {
		this.thumbnail = thumbnail;
	}

	public MultipartFile getVideo() {
		return video;
	}

	public void setVideo(MultipartFile video) {
		this.video = video;
	}

}