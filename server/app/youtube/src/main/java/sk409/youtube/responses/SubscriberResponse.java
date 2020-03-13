package sk409.youtube.responses;

import lombok.Data;
import sk409.youtube.models.Subscriber;

@Data
public class SubscriberResponse {

    private Long id;
    private Long userId;
    private Long channelId;

    public SubscriberResponse(final Subscriber subscriber) {
        id = subscriber.getId();
        userId = subscriber.getUserId();
        channelId = subscriber.getChannelId();
    }

}