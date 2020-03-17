package sk409.youtube.requests;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class VideoPopularChannelRequest {

    @NotNull
    private Long channelId;

    private List<Long> excludedIds;

    private Integer limit;

}