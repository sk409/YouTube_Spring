package sk409.youtube.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;

import sk409.youtube.graph.builders.EntityGraphBuilder;
import sk409.youtube.models.Rating;
import sk409.youtube.models.User;
import sk409.youtube.query.QueryComponents;
import sk409.youtube.query.sorting.VideoCommentSorting;
import sk409.youtube.query.specifications.UserSpecifications;
import sk409.youtube.query.specifications.VideoCommentRatingSpecifications;
import sk409.youtube.query.specifications.VideoCommentSpecifications;
import sk409.youtube.models.VideoComment;
import sk409.youtube.models.VideoCommentRating;
import sk409.youtube.repositories.VideoCommentRepository;
import sk409.youtube.responses.UserResponse;
import sk409.youtube.responses.VideoCommentRatingResponse;
import sk409.youtube.responses.VideoCommentResponse;
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

    private final UserService userService;
    private final VideoCommentRatingService videoCommentRatingService;
    private final VideoCommentRepository videoCommentRepository;

    public VideoCommentService(final EntityManager entityManager, final UserService userService,
            final VideoCommentRatingService videoCommentRatingService,
            final VideoCommentRepository videoCommentRepository) {
        super(entityManager);
        this.userService = userService;
        this.videoCommentRatingService = videoCommentRatingService;
        this.videoCommentRepository = videoCommentRepository;
    }

    @Override
    public Class<VideoComment> classLiteral() {
        return VideoComment.class;
    }

    public Optional<List<VideoComment>> findNextComments(final Long videoId, final Integer limit,
            final List<Long> exclude, final EntityGraphBuilder<VideoComment> videoCommentGraphBuilder) {
        final VideoCommentSpecifications videoCommentSpecifications = new VideoCommentSpecifications();
        videoCommentSpecifications.setParentIdIsNull(true);
        videoCommentSpecifications.setVideoIdEqual(videoId);
        if (exclude.size() != 0) {
            videoCommentSpecifications.setIdNotIn(exclude);
        }
        final QueryComponents<VideoComment> videoCommentQueryComponents = new QueryComponents<>();
        videoCommentQueryComponents.setSpecifications(videoCommentSpecifications);
        videoCommentQueryComponents.setLimit(limit);
        final Optional<List<VideoComment>> _videoComments = findAll(videoCommentQueryComponents);
        if (!_videoComments.isPresent()) {
            return Optional.ofNullable(null);
        }
        final List<VideoComment> videoComments = _videoComments.get();
        final List<Long> videoIds = videoComments.stream().map(videoComment -> videoComment.getId())
                .collect(Collectors.toList());
        final VideoCommentRatingSpecifications videoCommentRatingSpecifications = new VideoCommentRatingSpecifications();
        videoCommentRatingSpecifications.setVideoCommentIdIn(videoIds);
        final QueryComponents<VideoCommentRating> videoCommentRatingQueryComponents = new QueryComponents<>();
        videoCommentRatingQueryComponents.setSpecifications(videoCommentRatingSpecifications);
        final Optional<List<VideoCommentRating>> _videoCommentRatingList = videoCommentRatingService
                .findAll(videoCommentRatingQueryComponents);
        if (!_videoCommentRatingList.isPresent()) {
            return Optional.of(videoComments.subList(0, Math.min(videoComments.size(), limit)));
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

    public Optional<List<VideoComment>> findReplies(final Long videoCommentId, final Long newAfterVideoCommentId,
            final Integer limit) {
        final VideoCommentSpecifications videoCommentSpecifications = new VideoCommentSpecifications();
        videoCommentSpecifications.setParentIdEqual(videoCommentId);
        videoCommentSpecifications.setIdGreaterThan(newAfterVideoCommentId);
        final VideoCommentSorting videoCommentSorting = new VideoCommentSorting();
        videoCommentSorting.sortByIdAsc();
        final QueryComponents<VideoComment> videoCommentQueryComponents = new QueryComponents<>();
        videoCommentQueryComponents.setSpecifications(videoCommentSpecifications);
        videoCommentQueryComponents.setSorting(videoCommentSorting);
        videoCommentQueryComponents.setLimit(limit);
        return findAll(videoCommentQueryComponents);
    }

    public VideoComment save(final String text, final Long parentId, final Long userId, final Long videoId) {
        final VideoComment videoComment = new VideoComment(text, parentId, userId, videoId);
        videoCommentRepository.save(videoComment);
        return videoComment;
    }

    public Optional<List<VideoCommentResponse>> publicWatch(final List<VideoComment> videoComments,
            final String username) {
        final UserSpecifications userSpecifications = new UserSpecifications();
        userSpecifications.setUsernameEqual(username);
        final QueryComponents<User> userQueryComponents = new QueryComponents<>();
        userQueryComponents.setSpecifications(userSpecifications);
        final Optional<User> _user = userService.findOne(userQueryComponents);
        if (!_user.isPresent()) {
            return Optional.ofNullable(null);
        }
        final User user = _user.get();
        final List<Long> videoCommentIds = videoComments.stream().map(videoComment -> videoComment.getId())
                .collect(Collectors.toList());
        final VideoCommentRatingSpecifications videoCommentRatingSpecifications = new VideoCommentRatingSpecifications();
        videoCommentRatingSpecifications.setUserIdNotEqual(user.getId());
        videoCommentRatingSpecifications.setVideoCommentIdIn(videoCommentIds);
        final QueryComponents<VideoCommentRating> videoCommentRatingQueryComponents = new QueryComponents<>();
        videoCommentRatingQueryComponents.setSpecifications(videoCommentRatingSpecifications);
        final Optional<Map<Long, List<VideoCommentRating>>> _videoCommentRatingMap = videoCommentRatingService
                .findAll(videoCommentRatingQueryComponents).map(videoCommentRatingList -> videoCommentRatingList
                        .stream().collect(Collectors.groupingBy(VideoCommentRating::getVideoCommentId)));
        final VideoCommentRatingSpecifications videoCommentUserRatingSpecifications = new VideoCommentRatingSpecifications();
        videoCommentUserRatingSpecifications.setUserIdEqual(user.getId());
        videoCommentUserRatingSpecifications.setVideoCommentIdIn(videoCommentIds);
        final QueryComponents<VideoCommentRating> videoCommentUserRatingQueryComponents = new QueryComponents<>();
        videoCommentUserRatingQueryComponents.setSpecifications(videoCommentUserRatingSpecifications);
        final Optional<Map<Long, List<VideoCommentRating>>> _videoCommentUserRatingMap = videoCommentRatingService
                .findAll(videoCommentUserRatingQueryComponents)
                .map(userVideoCommentRatingList -> userVideoCommentRatingList.stream()
                        .collect(Collectors.groupingBy(VideoCommentRating::getVideoCommentId)));
        final List<VideoCommentResponse> videoCommentResponses = videoComments.stream().map(videoComment -> {
            final VideoCommentResponse videoCommentResponse = new VideoCommentResponse(videoComment);
            final UserResponse userResponse = new UserResponse(videoComment.getUser());
            videoCommentResponse.setUser(userResponse);
            final int childCount = videoComment.getChildren().size();
            videoCommentResponse.setChildCount((long) childCount);
            final Optional<List<VideoCommentRating>> _videoCommentHighRatingList = _videoCommentRatingMap
                    .map(videoCommentRatingMap -> videoCommentRatingMap.get(videoComment.getId()))
                    .map(videoCommentRatingList -> videoCommentRatingList == null ? null
                            : videoCommentRatingList.stream()
                                    .filter(videoCommentRating -> videoCommentRating.getRatingId() == Rating.highId)
                                    .collect(Collectors.toList()));
            final Long highRatingCount = _videoCommentHighRatingList
                    .map(videoCOmmentHighRatingList -> videoCOmmentHighRatingList == null ? 0L
                            : videoCOmmentHighRatingList.size())
                    .orElse(0L);
            videoCommentResponse.setHighRatingCount(highRatingCount);
            final Optional<List<VideoCommentRating>> _videoCommentLowRatingList = _videoCommentRatingMap
                    .map(videoCommentRatingMap -> videoCommentRatingMap.get(videoComment.getId()))
                    .map(videoCommentRatingList -> videoCommentRatingList == null ? null
                            : videoCommentRatingList.stream()
                                    .filter(videoCommentRating -> videoCommentRating.getRatingId() == Rating.lowId)
                                    .collect(Collectors.toList()));
            final Long lowRatingCount = _videoCommentLowRatingList.map(
                    videoCOmmenLowRatingList -> videoCOmmenLowRatingList == null ? 0L : videoCOmmenLowRatingList.size())
                    .orElse(0L);
            videoCommentResponse.setLowRatingCount(lowRatingCount);
            _videoCommentUserRatingMap.ifPresent(userVideoCommentRatingMap -> {
                final List<VideoCommentRating> userVideoCommentRatingList = userVideoCommentRatingMap
                        .get(videoComment.getId());
                if (userVideoCommentRatingList == null) {
                    return;
                }
                final VideoCommentRating userVideoCommentRating = userVideoCommentRatingList.get(0);
                final VideoCommentRatingResponse userVideoCommentRatingResponse = new VideoCommentRatingResponse(
                        userVideoCommentRating);
                videoCommentResponse.setUserRating(userVideoCommentRatingResponse);
            });
            return videoCommentResponse;
        }).collect(Collectors.toList());
        return Optional.of(videoCommentResponses);
    }
}