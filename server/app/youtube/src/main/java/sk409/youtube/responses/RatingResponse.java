package sk409.youtube.responses;

import lombok.Data;
import sk409.youtube.models.Rating;

@Data
public class RatingResponse {

    private Long id;
    private String name;

    public RatingResponse(final Rating rating) {
        id = rating.getId();
        name = rating.getName();
    }

}