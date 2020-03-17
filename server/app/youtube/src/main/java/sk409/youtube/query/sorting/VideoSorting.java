package sk409.youtube.query.sorting;

import sk409.youtube.models.Video;
import sk409.youtube.models.Video_;

public class VideoSorting extends Sorting<Video> {

    public void sortByCreatedAtAsc() {
        sortFunctions.add((root, query, builder) -> {
            query.orderBy(builder.asc(root.get(Video_.CREATED_AT)));
        });
    }

    public void sortByCreatedAtDesc() {
        sortFunctions.add((root, query, builder) -> {
            query.orderBy(builder.desc(root.get(Video_.CREATED_AT)));
        });
    }

    public void sortByViewsDesc() {
        sortFunctions.add((root, query, builder) -> {
            query.orderBy(builder.desc(root.get(Video_.VIEWS)));
        });
    }

}