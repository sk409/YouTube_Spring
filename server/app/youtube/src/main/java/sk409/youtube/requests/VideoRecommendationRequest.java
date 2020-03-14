package sk409.youtube.requests;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class VideoRecommendationRequest {

    @NotNull
    private Integer limit;

    private List<Long> excludedIds;

}