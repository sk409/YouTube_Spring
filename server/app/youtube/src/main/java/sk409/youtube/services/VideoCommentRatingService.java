package sk409.youtube.services;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;

import sk409.youtube.models.VideoCommentRating;
import sk409.youtube.query.QueryComponents;
import sk409.youtube.query.specifications.VideoCommentRatingSpecifications;
import sk409.youtube.repositories.VideoCommentRatingRepository;

@Service
public class VideoCommentRatingService extends QueryService<VideoCommentRating> {

    private final VideoCommentRatingRepository videoCommentRatingRepository;

    public VideoCommentRatingService(final EntityManager entityManager,
            final VideoCommentRatingRepository videoCommentRatingRepository) {
        super(entityManager);
        this.videoCommentRatingRepository = videoCommentRatingRepository;
    }

    @Override
    public Class<VideoCommentRating> classLiteral() {
        return VideoCommentRating.class;
    }

    public Optional<VideoCommentRating> delete(final Long id) {
        final VideoCommentRatingSpecifications videoCommentRatingSpecifications = new VideoCommentRatingSpecifications();
        videoCommentRatingSpecifications.setIdEqual(id);
        final QueryComponents<VideoCommentRating> videoCommentRatingQueryComponents = new QueryComponents<>();
        videoCommentRatingQueryComponents.setSpecifications(videoCommentRatingSpecifications);
        final Optional<VideoCommentRating> _videoCommentRating = findOne(videoCommentRatingQueryComponents);
        _videoCommentRating.ifPresent(videoCommentRating -> videoCommentRatingRepository.delete(videoCommentRating));
        return _videoCommentRating;
    }

    public Optional<VideoCommentRating> delete(final Long userId, final Long videoCommentId) {
        final VideoCommentRatingSpecifications videoCommentRatingSpecifications = new VideoCommentRatingSpecifications();
        videoCommentRatingSpecifications.setUserIdEqual(userId);
        videoCommentRatingSpecifications.setVideoCommentIdEqual(videoCommentId);
        final QueryComponents<VideoCommentRating> videoCommentRatingQueryComponents = new QueryComponents<>();
        videoCommentRatingQueryComponents.setSpecifications(videoCommentRatingSpecifications);
        final Optional<VideoCommentRating> _videoCommentRating = findOne(videoCommentRatingQueryComponents);
        _videoCommentRating.ifPresent(videoCommentRating -> videoCommentRatingRepository.delete(videoCommentRating));
        return _videoCommentRating;
    }

    public Optional<VideoCommentRating> findById(final Long id) {
        final VideoCommentRatingSpecifications videoCommentRatingSpecifications = new VideoCommentRatingSpecifications();
        videoCommentRatingSpecifications.setIdEqual(id);
        final QueryComponents<VideoCommentRating> videoCommentRatingQueryComponents = new QueryComponents<>();
        videoCommentRatingQueryComponents.setSpecifications(videoCommentRatingSpecifications);
        final Optional<VideoCommentRating> _videoCommentRating = findOne(videoCommentRatingQueryComponents);
        return _videoCommentRating;
    }

    public Optional<VideoCommentRating> findByUserIdAndVideoCommentId(final Long userId, final Long videoCommentId) {
        final VideoCommentRatingSpecifications videoCommentRatingSpecifications = new VideoCommentRatingSpecifications();
        videoCommentRatingSpecifications.setUserIdEqual(userId);
        videoCommentRatingSpecifications.setVideoCommentIdEqual(videoCommentId);
        final QueryComponents<VideoCommentRating> videoCommentRatingQueryComponents = new QueryComponents<>();
        videoCommentRatingQueryComponents.setSpecifications(videoCommentRatingSpecifications);
        final Optional<VideoCommentRating> _videoCommentRating = findOne(videoCommentRatingQueryComponents);
        return _videoCommentRating;
    }

    public VideoCommentRating save(final Long userId, final Long videoCommentId, final Long ratingId) {
        final VideoCommentRating videoCommentRating = new VideoCommentRating(userId, videoCommentId, ratingId);
        return save(videoCommentRating);
    }

    public VideoCommentRating save(final VideoCommentRating videoCommentRating) {
        videoCommentRatingRepository.save(videoCommentRating);
        return videoCommentRating;
    }

}