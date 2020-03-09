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
import lombok.AllArgsConstructor;
import lombok.Data;

@Service
public class VideoCommentService {

    @AllArgsConstructor
    @Data
    public class SummaryCount {

        private Long videoId;

        private Long count;

    }

    private final EntityManager entityManager;
    private final VideoCommentRepository videoCommentRepository;

    public VideoCommentService(final EntityManager entityManager, final VideoCommentRepository videoCommentRepository) {
        this.entityManager = entityManager;
        this.videoCommentRepository = videoCommentRepository;
    }

    public List<SummaryCount> countByVideoIdInGroupByVideoId(final Long... videoIds) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Tuple> query = builder.createTupleQuery();
        final Root<VideoComment> root = query.from(VideoComment.class);
        final Path<Long> videoId = root.get(VideoComment_.VIDEO_ID);
        final Expression<Long> count = builder.count(root.get(VideoComment_.VIDEO_ID));
        query.select(builder.tuple(videoId, count)).where(root.get(VideoComment_.VIDEO_ID).in((Object[]) videoIds))
                .groupBy(videoId);
        final TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
        final List<SummaryCount> counts = typedQuery.getResultList().stream()
                .map(tuple -> new SummaryCount(tuple.get(videoId), tuple.get(count))).collect(Collectors.toList());
        return counts;
    }

    public Optional<List<VideoComment>> findByVideoId(final Long videoId) {
        return videoCommentRepository.findByVideoId(videoId);
    }

    public List<VideoComment> findByVideoIdOrderByIdDescLimit(Long videoId, Long limit) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<VideoComment> query = builder.createQuery(VideoComment.class);
        final Root<VideoComment> root = query.from(VideoComment.class);
        final Path<Long> videoIdPath = root.get(VideoComment_.VIDEO_ID);
        final Path<Long> idPath = root.get(VideoComment_.ID);
        query.select(root).where(builder.equal(videoIdPath, videoId)).orderBy(builder.desc(idPath));
        final TypedQuery<VideoComment> typedQuery = entityManager.createQuery(query);
        final List<VideoComment> videoComments = typedQuery.getResultList();
        return videoComments;
    }

    public List<VideoComment> findByVideoIdLessThanIdOrderByIdDescLimit(Long videoId, Long id, Long limit) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<VideoComment> query = builder.createQuery(VideoComment.class);
        final Root<VideoComment> root = query.from(VideoComment.class);
        final Path<Long> videoIdPath = root.get(VideoComment_.VIDEO_ID);
        final Path<Long> idPath = root.get(VideoComment_.ID);
        query.select(root).where(builder.and(builder.equal(videoIdPath, videoId), builder.lessThan(idPath, id)))
                .orderBy(builder.desc(idPath));
        final TypedQuery<VideoComment> typedQuery = entityManager.createQuery(query);
        final List<VideoComment> videoComments = typedQuery.getResultList();
        return videoComments;
    }

}