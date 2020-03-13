package sk409.youtube.query.sorting;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import lombok.Data;
import sk409.youtube.models.VideoComment;
import sk409.youtube.models.VideoComment_;

@Data
public class VideoCommentSorting implements Sorting<VideoComment> {

    private Boolean idAsc;

    public void sort(final Root<VideoComment> root, final CriteriaQuery<VideoComment> query,
            final CriteriaBuilder builder) {
        sortByIdAsc(root, query, builder);
    }

    private void sortByIdAsc(final Root<VideoComment> root, final CriteriaQuery<VideoComment> query,
            final CriteriaBuilder builder) {
        if (idAsc == null || !idAsc) {
            return;
        }
        query.orderBy(builder.asc(root.get(VideoComment_.ID)));
    }

}