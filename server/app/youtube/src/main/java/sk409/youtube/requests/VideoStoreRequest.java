package sk409.youtube.requests;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

public class VideoStoreRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Size(min = 1, max = 100)
	@Getter
	@Setter
	private String title;

	@Size(max = 5000)
	@Getter
	@Setter
	private String overview;

	@NotNull
	@Getter
	@Setter
	private Float duration;

	@NotNull
	@Size(min = 1, max = 255)
	@Getter
	@Setter
	private String uniqueId;

	@NotNull
	@Getter
	@Setter
	private MultipartFile thumbnail;

	@NotNull
	@Getter
	@Setter
	private MultipartFile video;

}