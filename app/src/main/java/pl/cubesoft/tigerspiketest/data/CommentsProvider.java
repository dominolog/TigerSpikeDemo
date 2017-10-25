package pl.cubesoft.tigerspiketest.data;



import pl.cubesoft.tigerspiketest.data.model.Comment;
import pl.cubesoft.tigerspiketest.data.model.CommentPool;
import rx.Observable;

/**
 * Created by CUBESOFT on 19.09.2017.
 */

public interface CommentsProvider {
    Observable<CommentPool> getComments(String commentPoolId);
    Observable<CommentPool> loadMore(String commentPoolId, CommentPool commentPool, int page);

    boolean canComment(CommentPool comentPool);

    Observable<Comment> addComment(String commentPoolId, String comment);


}
