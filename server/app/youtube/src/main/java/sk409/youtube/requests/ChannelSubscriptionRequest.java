package sk409.youtube.requests;

import java.util.List;

import lombok.Data;

@Data
public class ChannelSubscriptionRequest {

    private List<Long> excludedChannelIds;

    private Integer limit;

}