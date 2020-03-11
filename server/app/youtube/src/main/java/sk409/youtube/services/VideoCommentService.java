package sk409.youtube.services;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;

import sk409.youtube.models.VideoComment;
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

    private final VideoCommentRepository videoCommentRepository;

    public VideoCommentService(final EntityManager entityManager, final VideoCommentRepository videoCommentRepository) {
        super(entityManager);
        this.videoCommentRepository = videoCommentRepository;
    }

    @Override
    public Class<VideoComment> classLiteral() {
        return VideoComment.class;
    }

    public VideoComment save(final String text, final Long parentId, final Long userId, final Long videoId) {
        final VideoComment videoComment = new VideoComment(text, parentId, userId, videoId);
        videoCommentRepository.save(videoComment);
        return videoComment;
    }

}