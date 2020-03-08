package sk409.youtube.services;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import sk409.youtube.models.Video;
import sk409.youtube.repositories.VideoRepository;

@Service
public class VideoService {

    private final FileService fileService;
    private final PathService pathService;
    private final VideoRepository videoRepository;

    public VideoService(final FileService fileService, final PathService pathService,
            final VideoRepository videoRepository) {
        this.fileService = fileService;
        this.pathService = pathService;
        this.videoRepository = videoRepository;
    }

    public List<Video> findByChannelId(final Long channelId) {
        final Optional<List<Video>> _videos = videoRepository.findByChannelId(channelId);
        return _videos.isPresent() ? _videos.get() : null;
    }

    public Video findByUniqueId(final String uniqueId) {
        final Optional<Video> _video = videoRepository.findByUniqueId(uniqueId);
        return _video.isPresent() ? _video.get() : null;
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

    public void save(final Video video) {
        videoRepository.save(video);
    }

}