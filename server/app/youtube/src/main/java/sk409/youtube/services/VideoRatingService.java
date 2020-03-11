package sk409.youtube.services;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Data;

import sk409.youtube.models.VideoRating;
import sk409.youtube.repositories.VideoRatingRepository;

@Service
public class VideoRatingService extends QueryService<VideoRating> {

    @AllArgsConstructor
    @Data
    public class SummaryCount {

        private Long videoId;

        private Long ratingId;

        private Long count;

    }

    private final VideoRatingRepository videoRatingRepository;

    public VideoRatingService(final EntityManager entityManager, final VideoRatingRepository videoRatingRepository) {
        super(entityManager);
        this.videoRatingRepository = videoRatingRepository;
    }

    @Override
    public Class<VideoRating> classLiteral() {
        return VideoRating.class;
    }

    public VideoRating delete(VideoRating videoRating) {
        videoRatingRepository.delete(videoRating);
        return videoRating;
    }

    public VideoRating save(Long userId, Long videoId, Long ratingId) {
        final VideoRating videoRating = new VideoRating(userId, videoId, ratingId);
        videoRatingRepository.save(videoRating);
        return videoRating;
    }

}