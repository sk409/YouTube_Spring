package sk409.youtube.query.sorting;

import sk409.youtube.models.Video;
import sk409.youtube.models.Video_;

public class VideoSorting extends Sorting<Video> {

    public void sortByViewsDesc() {
        sortFunctions.add((root, query, builder) -> {
            query.orderBy(builder.desc(root.get(Video_.VIEWS)));
        });
    }

}