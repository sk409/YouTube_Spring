package sk409.youtube.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Data;

import sk409.youtube.models.VideoRating;
import sk409.youtube.models.VideoRating_;
import sk409.youtube.repositories.VideoRatingRepository;

@Service
public class VideoRatingService {

    @AllArgsConstructor
    @Data
    public class SummaryCount {

        private Long videoId;

        private Long ratingId;

        private Long count;

    }

    private final EntityManager entityManager;
    private final VideoRatingRepository videoRatingRepository;

    public VideoRatingService(final EntityManager entityManager, final VideoRatingRepository videoRatingRepository) {
        this.entityManager = entityManager;
        this.videoRatingRepository = videoRatingRepository;
    }

    public List<SummaryCount> countByVideoIdAndNotUserIdGroupByVideoIdAndRatingId(final Long videoId,
            final Long userId) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Tuple> query = builder.createTupleQuery();
        final Root<VideoRating> root = query.from(VideoRating.class);
        final Path<Long> userIdPath = root.get(VideoRating_.USER_ID);
        final Path<Long> videoIdPath = root.get(VideoRating_.VIDEO_ID);
        final Path<Long> ratingIdPath = root.get(VideoRating_.RATING_ID);
        final Expression<Long> count = builder.count(root.get(VideoRating_.VIDEO_ID));
        query.select(builder.tuple(videoIdPath, ratingIdPath, count))
                .where(builder.and(builder.equal(videoIdPath, videoId), builder.notEqual(
                        userIdPath, userId)))
                .groupBy(videoIdPath, ratingIdPath);
        final TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
        final List<SummaryCount> counts = typedQuery.getResultList().stream()
                .map(tuple -> new SummaryCount(tuple.get(videoIdPath), tuple.get(ratingIdPath), tuple.get(count)))
                .collect(Collectors.toList());
        return counts;
    }

    public List<SummaryCount> countByVideoIdGroupByVideoIdAndRatingId(final Long id) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Tuple> query = builder.createTupleQuery();
        final Root<VideoRating> root = query.from(VideoRating.class);
        final Path<Long> videoId = root.get(VideoRating_.VIDEO_ID);
        final Path<Long> ratingId = root.get(VideoRating_.RATING_ID);
        final Expression<Long> count = builder.count(root.get(VideoRating_.VIDEO_ID));
        query.select(builder.tuple(videoId, ratingId, count)).where(builder.equal(videoId, id)).groupBy(videoId,
                ratingId);
        final TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
        final List<SummaryCount> counts = typedQuery.getResultList().stream()
                .map(tuple -> new SummaryCount(tuple.get(videoId), tuple.get(ratingId), tuple.get(count)))
                .collect(Collectors.toList());
        return counts;
    }

    public List<SummaryCount> countByVideoIdInGroupByVideoIdAndRatingId(final Long... videoIds) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Tuple> query = builder.createTupleQuery();
        final Root<VideoRating> root = query.from(VideoRating.class);
        final Path<Long> videoId = root.get(VideoRating_.VIDEO_ID);
        final Path<Long> ratingId = root.get(VideoRating_.RATING_ID);
        final Expression<Long> count = builder.count(root.get(VideoRating_.VIDEO_ID));
        query.select(builder.tuple(videoId, ratingId, count)).where(videoId.in((Object[]) videoIds)).groupBy(videoId,
                ratingId);
        final TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
        final List<SummaryCount> counts = typedQuery.getResultList().stream()
                .map(tuple -> new SummaryCount(tuple.get(videoId), tuple.get(ratingId), tuple.get(count)))
                .collect(Collectors.toList());
        return counts;
    }

    public VideoRating destroy(VideoRating videoRating) {
        videoRatingRepository.delete(videoRating);
        return videoRating;
    }

    public Optional<VideoRating> findFirstByUserIdAndVideoId(Long userId, Long videoId) {
        return videoRatingRepository.findFirstByUserIdAndVideoId(userId, videoId);
    }

    public VideoRating save(Long userId, Long videoId, Long ratingId) {
        final VideoRating videoRating = new VideoRating(userId, videoId, ratingId);
        videoRatingRepository.save(videoRating);
        return videoRating;
    }

}