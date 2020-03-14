package sk409.youtube.requests;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class VideoCommentRepliesRequest {

    @NotNull
    private Long videoCommentId;

    private Long newAfterVideoCommentId;

    private Integer limit;

}