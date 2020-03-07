package sk409.youtube.services;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import sk409.youtube.models.Channel;
import sk409.youtube.models.Video;
import sk409.youtube.repositories.VideoRepository;

@Service
public class VideoService {

    private final ChannelService channelService;
    private final FileService fileService;
    private final PathService pathService;
    private final VideoRepository videoRepository;

    public VideoService(ChannelService channelService, FileService fileService, PathService pathService, VideoRepository videoRepository) {
        this.channelService = channelService;
        this.fileService = fileService;
        this.pathService = pathService;
        this.videoRepository = videoRepository;
    }

    public List<Video> findByChannelId(Long channelId) {
        final Optional<List<Video>> _videos = videoRepository.findByChannelId(channelId);
        return _videos.isPresent() ? _videos.get() : null;
    }

    public Video save(String title, String overview, Float duration, String uniqueId, Long channelId, MultipartFile videoFile, MultipartFile thumbnailFile) throws IOException{
        final Channel channel = channelService.findById(channelId);
        if (channel == null) {
            return null;
        }
        final Video video = new Video(title, overview, 0L, duration, "", "", uniqueId, channel);
        save(video);
        final String basePath = Paths.get(pathService.getVideosPath().toString(), String.valueOf(channelId),String.valueOf(video.getId())).toString();
        final String thumbnailPath = Paths.get(basePath, "thumbnail", thumbnailFile.getOriginalFilename()).toString();
        fileService.write(thumbnailPath, thumbnailFile.getBytes());
        final String videoPath = Paths.get(basePath, videoFile.getOriginalFilename()).toString();
        try {
            fileService.write(videoPath, videoFile.getBytes());
        } catch(IOException exception) {
            return null;
        }
        video.setVideoPath(videoPath.substring(pathService.getStaticPath().toString().length()));
        video.setThumbnailPath(thumbnailPath.substring(pathService.getStaticPath().toString().length()));
        save(video);
        return video;
    }

    public void save(Video video) {
        videoRepository.save(video);
    }

}