package sk409.youtube.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import sk409.youtube.models.VideoComment;
import sk409.youtube.repositories.VideoCommentRepository;

@Service
public class VideoCommentService {

    private final VideoCommentRepository videoCommentRepository;

    public VideoCommentService(VideoCommentRepository videoCommentRepository) {
        this.videoCommentRepository = videoCommentRepository;
    }

    public Long countByVideoId(Long videoId) {
        final Optional<Long> counts = videoCommentRepository.countByVideoId(videoId);
        return counts.isPresent() ? counts.get() : null;
    }

    public List<VideoComment> findByVideoId(Long videoId) {
        final Optional<List<VideoComment>> videoComments = videoCommentRepository.findByVideoId(videoId);
        return videoComments.isPresent() ? videoComments.get() : null;
    }
 
}