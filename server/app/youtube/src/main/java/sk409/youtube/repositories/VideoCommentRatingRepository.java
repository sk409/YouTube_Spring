package sk409.youtube.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import sk409.youtube.models.VideoCommentRating;

@Repository
public interface VideoCommentRatingRepository extends CrudRepository<VideoCommentRating, Long> {
}