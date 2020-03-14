package sk409.youtube.requests;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class VideoCommentNextCommentsRequest {

    @NotNull
    private Integer limit;

    @NotNull
    private Long videoId;

    @NotNull
    private List<Long> exclude = new ArrayList<>();
}