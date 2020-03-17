package sk409.youtube.requests;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class VideoOldChannelRequest {
    @NotNull
    private Long channelId;

    private Integer limit;

    private Long newAfter;
}