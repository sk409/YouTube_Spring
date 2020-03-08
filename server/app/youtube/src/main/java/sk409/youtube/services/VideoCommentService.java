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

import sk409.youtube.models.VideoComment;
import sk409.youtube.models.VideoComment_;
import sk409.youtube.repositories.VideoCommentRepository;
import lombok.Getter;
import lombok.Setter;

@Service
public class VideoCommentService {

    public class SummaryCountByVideoIdGroupByVideoId {

        @Getter
        @Setter
        private Long videoId;

        @Getter
        @Setter
        private Long count;

        public SummaryCountByVideoIdGroupByVideoId(Long videoId, Long count) {
            this.videoId = videoId;
            this.count = count;
        }

    }

    private final EntityManager entityManager;

    private final VideoCommentRepository videoCommentRepository;

    public VideoCommentService(final EntityManager entityManager, final VideoCommentRepository videoCommentRepository) {
        this.entityManager = entityManager;
        this.videoCommentRepository = videoCommentRepository;
    }

    public List<SummaryCountByVideoIdGroupByVideoId> countByVideoIdInGroupByVideoId(final Long... videoIds) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Tuple> query = builder.createTupleQuery();
        final Root<VideoComment> root = query.from(VideoComment.class);
        final Path<Long> videoId = root.get(VideoComment_.VIDEO_ID);
        final Expression<Long> count = builder.count(root.get(VideoComment_.VIDEO_ID));
        query.select(builder.tuple(videoId, count)).where(root.get(VideoComment_.VIDEO_ID).in((Object[]) videoIds))
                .groupBy(videoId);
        final TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
        final List<SummaryCountByVideoIdGroupByVideoId> counts = typedQuery.getResultList().stream()
                .map(tuple -> new SummaryCountByVideoIdGroupByVideoId(tuple.get(videoId), tuple.get(count)))
                .collect(Collectors.toList());
        return counts;
    }

    public List<VideoComment> findByVideoId(final Long videoId) {
        final Optional<List<VideoComment>> videoComments = videoCommentRepository.findByVideoId(videoId);
        return videoComments.isPresent() ? videoComments.get() : null;
    }

}