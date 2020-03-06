package sk409.youtube.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import sk409.youtube.models.Channel;
import sk409.youtube.models.Video;
import sk409.youtube.repositories.VideoRepository;

@Service
public class VideoService {

    private final VideoRepository videoRepository;

    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public Video save(String title, String overview, String uniqueId, Channel channel) {
        final Video video = new Video(title, overview, uniqueId, channel);
        videoRepository.save(video);
        return video;
    }

}