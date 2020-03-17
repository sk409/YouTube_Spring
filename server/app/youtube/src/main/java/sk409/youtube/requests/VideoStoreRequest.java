package sk409.youtube.requests;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
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
	private Long channelId;

	@NotNull
	private MultipartFile thumbnail;

	@NotNull
	private MultipartFile video;

}