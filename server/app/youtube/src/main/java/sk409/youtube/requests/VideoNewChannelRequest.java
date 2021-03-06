package sk409.youtube.requests;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class VideoNewChannelRequest {

    @NotNull
    private Long channelId;

    private Integer limit;

    private Long oldBeforeId;

}