package sk409.youtube.query.sorting;

import sk409.youtube.models.VideoComment;
import sk409.youtube.models.VideoComment_;

public class VideoCommentSorting extends Sorting<VideoComment> {

    public void sortByIdAsc() {
        sortFunctions.add((root, query, builder) -> {
            query.orderBy(builder.asc(root.get(VideoComment_.ID)));
        });
    }

}