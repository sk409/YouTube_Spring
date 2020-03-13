package sk409.youtube.requests;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class VideoCommentFetchRepliesRequest {

    @NotNull
    private Long videoCommentId;

    private Long newAfterVideoCommentId;

    @NotNull
    private Integer limit;

}