package sk409.youtube.services;

import java.util.List;
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

@Service
public class VideoRatingService {

    @AllArgsConstructor
    @Data
    public class SummaryCountByVideoIdGroupByVideoIdAndRatingId {

        private Long videoId;

        private Long ratingId;

        private Long count;

    }

    private EntityManager entityManager;

    public VideoRatingService(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<SummaryCountByVideoIdGroupByVideoIdAndRatingId> countByVideoIdInGroupByVideoIdAndRatingId(
            final Long... videoIds) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Tuple> query = builder.createTupleQuery();
        final Root<VideoRating> root = query.from(VideoRating.class);
        final Path<Long> videoId = root.get(VideoRating_.VIDEO_ID);
        final Path<Long> ratingId = root.get(VideoRating_.RATING_ID);
        final Expression<Long> count = builder.count(root.get(VideoRating_.VIDEO_ID));
        query.select(builder.tuple(videoId, ratingId, count)).where(videoId.in((Object[]) videoIds)).groupBy(videoId, ratingId);
        final TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
        final List<SummaryCountByVideoIdGroupByVideoIdAndRatingId> counts = typedQuery.getResultList().stream()
                .map(tuple -> new SummaryCountByVideoIdGroupByVideoIdAndRatingId(tuple.get(videoId),
                        tuple.get(ratingId), tuple.get(count)))
                .collect(Collectors.toList());
        return counts;
    }

}