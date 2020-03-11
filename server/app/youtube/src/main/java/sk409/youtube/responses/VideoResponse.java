package sk409.youtube.responses;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import sk409.youtube.models.Video;

@Data
public class VideoResponse {

    private Long id;
    private String title;
    private String overview;
    private Long views;
    private Float duration;
    private String videoPath;
    private String thumbnailPath;
    private String uniqueId;
    private Long channelId;
    private Date createdAt;
    private Date updatedAt;

    private Integer commentCount;
    private Float highRatingRate;

    @JsonBackReference("ChannelResponse")
    private ChannelResponse channel;

    @JsonManagedReference("video")
    private List<VideoCommentResponse> comments;

    @JsonManagedReference("video")
    private List<VideoRatingResponse> rating;

    public VideoResponse(final Video video) {
        id = video.getId();
        title = video.getTitle();
        overview = video.getOverview();
        views = video.getViews();
        duration = video.getDuration();
        videoPath = video.getVideoPath();
        thumbnailPath = video.getThumbnailPath();
        uniqueId = video.getUniqueId();
        channelId = video.getChannelId();
        createdAt = video.getCreatedAt();
        updatedAt = video.getUpdatedAt();
    }

}