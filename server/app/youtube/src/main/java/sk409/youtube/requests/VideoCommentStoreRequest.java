package sk409.youtube.requests;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class VideoCommentStoreRequest {

    @NotNull
    @NotEmpty
    private String text;

    private Long parentId;

    @NotNull
    private Long videoId;

}