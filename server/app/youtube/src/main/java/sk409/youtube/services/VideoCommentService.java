package sk409.youtube.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityGraph;
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
public class VideoCommentService extends QueryService<VideoComment> {

    @AllArgsConstructor
    @Data
    public class SummaryCount {

        private Long videoId;

        private Long count;

    }

    private final EntityManager entityManager;
    private final VideoCommentRepository videoCommentRepository;

    public VideoCommentService(final EntityManager entityManager, final VideoCommentRepository videoCommentRepository) {
        super(entityManager);
        this.entityManager = entityManager;
        this.videoCommentRepository = videoCommentRepository;
    }

    @Override
    public Class<VideoComment> classLiteral() {
        return VideoComment.class;
    }

    public SummaryCount countByVideoIdGroupByVideoId(final Long videoId) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Tuple> query = builder.createTupleQuery();
        final Root<VideoComment> root = query.from(VideoComment.class);
        final Path<Long> videoIdPath = root.get(VideoComment_.VIDEO_ID);
        final Expression<Long> count = builder.count(root.get(VideoComment_.VIDEO_ID));
        query.select(builder.tuple(videoIdPath, count)).where(builder.equal(videoIdPath, videoId)).groupBy(videoIdPath);
        final TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
        final Tuple tuple = typedQuery.getSingleResult();
        final SummaryCount summaryCount = new SummaryCount(tuple.get(videoIdPath), tuple.get(count));
        return summaryCount;
    }

    public List<SummaryCount> countByVideoIdInGroupByVideoId(final Long... videoIds) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Tuple> query = builder.createTupleQuery();
        final Root<VideoComment> root = query.from(VideoComment.class);
        final Path<Long> videoIdPath = root.get(VideoComment_.VIDEO_ID);
        final Expression<Long> count = builder.count(root.get(VideoComment_.VIDEO_ID));
        query.select(builder.tuple(videoIdPath, count)).where(root.get(VideoComment_.VIDEO_ID).in((Object[]) videoIds))
                .groupBy(videoIdPath);
        final TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
        final List<SummaryCount> summaryCounts = typedQuery.getResultList().stream()
                .map(tuple -> new SummaryCount(tuple.get(videoIdPath), tuple.get(count))).collect(Collectors.toList());
        return summaryCounts;
    }

    public VideoComment save(final String text, final Long parentId, final Long userId, final Long videoId) {
        final VideoComment videoComment = new VideoComment(text, parentId, userId, videoId);
        videoCommentRepository.save(videoComment);
        return videoComment;
    }

}