package sk409.youtube.requests;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class VideoCommentFetchNextCommentsRequest {

    @NotNull
    private Long limit;

    private Long oldBefore;

    @NotNull
    private Long videoId;

}