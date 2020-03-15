package sk409.youtube.services;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import sk409.youtube.graph.builders.EntityGraphBuilder;
import sk409.youtube.graph.builders.VideoGraphBuilder;
import sk409.youtube.models.Video;
import sk409.youtube.query.QueryComponents;
import sk409.youtube.query.sorting.VideoSorting;
import sk409.youtube.query.specifications.VideoSpecifications;
import sk409.youtube.repositories.VideoRepository;

@Service
public class VideoService extends QueryService<Video> {

    private final FileService fileService;
    private final PathService pathService;
    private final VideoRepository videoRepository;

    public VideoService(final EntityManager entityManager, final FileService fileService, final PathService pathService,
            final VideoRepository videoRepository) {
        super(entityManager);
        this.fileService = fileService;
        this.pathService = pathService;
        this.videoRepository = videoRepository;
    }

    @Override
    public Class<Video> classLiteral() {
        return Video.class;
    }

    public Optional<Video> findByUniqueId(final String uniqueId, final String... nodes) {
        final VideoSpecifications videoSpecifications = new VideoSpecifications();
        videoSpecifications.setUniqueIdEqual(uniqueId);
        final EntityGraphBuilder<Video> videoGraphBuilder = VideoGraphBuilder.make(nodes);
        final QueryComponents<Video> videoQueryComponents = new QueryComponents<>();
        videoQueryComponents.setSpecifications(videoSpecifications);
        videoQueryComponents.setEntityGraphBuilder(videoGraphBuilder);
        final Optional<Video> _video = findOne(videoQueryComponents);
        return _video;
    }

    public Optional<List<Video>> findNewVideosChannel(final Long channelId, final Integer limit) {
        final VideoSpecifications videoSepcifications = new VideoSpecifications();
        videoSepcifications.setChannelIdEqual(channelId);
        final VideoSorting videoSorting = new VideoSorting();
        videoSorting.sortByCreatedAtDesc();
        final QueryComponents<Video> videoQueryComponents = new QueryComponents<>();
        videoQueryComponents.setSpecifications(videoSepcifications);
        videoQueryComponents.setSorting(videoSorting);
        videoQueryComponents.setLimit(limit);
        final Optional<List<Video>> _videos = findAll(videoQueryComponents);
        return _videos;
    }

    public Optional<List<Video>> findPopularChannel(final Long channelId, final Integer limit) {
        final VideoSpecifications videoSepcifications = new VideoSpecifications();
        videoSepcifications.setChannelIdEqual(channelId);
        final VideoSorting videoSorting = new VideoSorting();
        videoSorting.sortByViewsDesc();
        final QueryComponents<Video> videoQueryComponents = new QueryComponents<>();
        videoQueryComponents.setSpecifications(videoSepcifications);
        videoQueryComponents.setSorting(videoSorting);
        videoQueryComponents.setLimit(limit);
        final Optional<List<Video>> _videos = findAll(videoQueryComponents);
        return _videos;
    }

    public Optional<List<Video>> findRecommendation(final Long userId, final QueryComponents<Video> options) {
        final QueryComponents<Video> videoQueryComponents = new QueryComponents<>();
        videoQueryComponents.assign(options);
        final Optional<List<Video>> _videos = findAll(videoQueryComponents);
        return _videos;
    }

    public Video save(final String title, final String overview, final Float duration, final String uniqueId,
            final Long channelId, final MultipartFile videoFile, final MultipartFile thumbnailFile) throws IOException {
        final Video video = new Video(title, overview, 0L, duration, "", "", uniqueId, channelId);
        save(video);
        final String basePath = Paths
                .get(pathService.getVideosPath().toString(), String.valueOf(channelId), String.valueOf(video.getId()))
                .toString();
        final String thumbnailPath = Paths.get(basePath, "thumbnail", thumbnailFile.getOriginalFilename()).toString();
        fileService.write(thumbnailPath, thumbnailFile.getBytes());
        final String videoPath = Paths.get(basePath, videoFile.getOriginalFilename()).toString();
        try {
            fileService.write(videoPath, videoFile.getBytes());
        } catch (IOException exception) {
            return null;
        }
        video.setVideoPath(videoPath.substring(pathService.getStaticPath().toString().length()));
        video.setThumbnailPath(thumbnailPath.substring(pathService.getStaticPath().toString().length()));
        save(video);
        return video;
    }

    public Video save(final Video video) {
        videoRepository.save(video);
        return video;
    }

}