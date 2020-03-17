package sk409.youtube.requests;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class VideoNewChannelRequest {

    private Integer limit;

    private Long oldBeforeId;

}