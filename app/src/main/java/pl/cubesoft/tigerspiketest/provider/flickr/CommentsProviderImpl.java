package pl.cubesoft.tigerspiketest.provider.flickr;

import pl.cubesoft.tigerspiketest.data.CommentsProvider;
import pl.cubesoft.tigerspiketest.data.model.Comment;
import pl.cubesoft.tigerspiketest.data.model.CommentPool;
import rx.Observable;

/**
 * Created by CUBESOFT on 19.09.2017.
 */

class CommentsProviderImpl implements CommentsProvider {
    private final APIServiceWrapper apiServiceWrapper;

    public CommentsProviderImpl(APIServiceWrapper apiServiceWrapper) {
        this.apiServiceWrapper = apiServiceWrapper;
    }

    @Override
    public Observable<CommentPool> getComments(String commentPoolId) {
        return apiServiceWrapper.getPhotoComments(commentPoolId).map(CommentPoolImpl::new);
    }

    @Override
    public Observable<CommentPool> loadMore(String commentPoolId, CommentPool commentPool, int page) {
        return Observable.just(commentPool);
    }

    @Override
    public boolean canComment(CommentPool comentPool) {
        return comentPool.canComment();
    }

    @Override
    public Observable<Comment> addComment(String commentPoolId, String comment) {
        return apiServiceWrapper.addComment(commentPoolId, comment).map(CommentPoolImpl.CommentImpl::new);
    }
}
