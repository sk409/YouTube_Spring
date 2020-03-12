package sk409.youtube.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Service;

import sk409.youtube.graph.builders.EntityGraphBuilder;
import sk409.youtube.models.Rating;
import sk409.youtube.models.Video;
import sk409.youtube.models.Video_;
import sk409.youtube.query.QueryComponents;
import sk409.youtube.query.specifications.VideoCommentRatingSpecifications;
import sk409.youtube.query.specifications.VideoCommentSpecifications;
import sk409.youtube.models.VideoComment;
import sk409.youtube.models.VideoComment_;
import sk409.youtube.models.VideoCommentRating;
import sk409.youtube.models.VideoCommentRating_;
import sk409.youtube.repositories.VideoCommentRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
public class VideoCommentService extends QueryService<VideoComment> {

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public class SummaryCount {

        private long id;

        private long count;

    }

    private final EntityManager entityManager;
    private final VideoCommentRatingService videoCommentRatingService;
    private final VideoCommentRepository videoCommentRepository;

    public VideoCommentService(final EntityManager entityManager,
            final VideoCommentRatingService videoCommentRatingService,
            final VideoCommentRepository videoCommentRepository) {
        super(entityManager);
        this.entityManager = entityManager;
        this.videoCommentRatingService = videoCommentRatingService;
        this.videoCommentRepository = videoCommentRepository;
    }

    @Override
    public Class<VideoComment> classLiteral() {
        return VideoComment.class;
    }

    public Optional<List<VideoComment>> findPopularComments(final Long videoId, final Integer limit,
            final EntityGraphBuilder<VideoComment> videoCommentGraphBuilder) {
        final VideoCommentSpecifications videoCommentSpecifications = new VideoCommentSpecifications();
        videoCommentSpecifications.setVideoIdEqual(videoId);
        final QueryComponents<VideoComment> videoCommentQueryComponents = new QueryComponents<>();
        videoCommentQueryComponents.setSpecifications(videoCommentSpecifications);
        final Optional<List<VideoComment>> _videoComments = findAll(videoCommentQueryComponents);
        if (!_videoComments.isPresent()) {
            return Optional.ofNullable(null);
        }
        final List<VideoComment> videoComments = _videoComments.get();
        final Long[] videoIds = videoComments.stream().map(videoComment -> videoComment.getId())
                .collect(Collectors.toList()).toArray(new Long[] {});
        final VideoCommentRatingSpecifications videoCommentRatingSpecifications = new VideoCommentRatingSpecifications();
        videoCommentRatingSpecifications.setVideoCommentIdIn(videoIds);
        final QueryComponents<VideoCommentRating> videoCommentRatingQueryComponents = new QueryComponents<>();
        videoCommentRatingQueryComponents.setSpecifications(videoCommentRatingSpecifications);
        final Optional<List<VideoCommentRating>> _videoCommentRatingList = videoCommentRatingService
                .findAll(videoCommentRatingQueryComponents);
        if (!_videoCommentRatingList.isPresent()) {
            return Optional.of(videoComments.subList(0, limit));
        }
        final List<VideoCommentRating> videoCommentRatingList = _videoCommentRatingList.get();
        final Map<Long, List<VideoCommentRating>> videoCommentRatingMap = videoCommentRatingList.stream()
                .collect(Collectors.groupingBy(VideoCommentRating::getVideoCommentId));
        final Map<Long, Integer> scoreMap = new HashMap<>();
        videoComments.stream().forEach(videoComment -> {
            final List<VideoCommentRating> vcrl = videoCommentRatingMap.get(videoComment.getId());
            if (vcrl == null) {
                scoreMap.put(videoComment.getId(), 0);
                return;
            }
            final Integer score = vcrl.stream().reduce(0, (s, videoCommentRating) -> {
                final Integer add = videoCommentRating.getRatingId() == Rating.highId ? 1 : -1;
                return s + add;
            }, (s1, s2) -> s1 + s2);
            scoreMap.put(videoComment.getId(), score);
        });
        final List<Entry<Long, Integer>> scoreEntries = new ArrayList<>(scoreMap.entrySet());
        Collections.sort(scoreEntries, new Comparator<Entry<Long, Integer>>() {
            @Override
            public int compare(Entry<Long, Integer> o1, Entry<Long, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        final List<VideoComment> resultList = scoreEntries.stream().map(scoreEntry -> {
            return videoComments.stream().filter(videoComment -> videoComment.getId() == scoreEntry.getKey())
                    .findFirst().get();
        }).collect(Collectors.toList());
        return Optional.of(resultList);
    }

    public VideoComment save(final String text, final Long parentId, final Long userId, final Long videoId) {
        final VideoComment videoComment = new VideoComment(text, parentId, userId, videoId);
        videoCommentRepository.save(videoComment);
        return videoComment;
    }
}