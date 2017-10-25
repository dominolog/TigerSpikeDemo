package pl.cubesoft.tigerspiketest.provider.flickr;

import java.util.Date;

import pl.cubesoft.tigerspiketest.data.model.Comment;
import pl.cubesoft.tigerspiketest.data.model.CommentPool;
import pl.cubesoft.tigerspiketest.data.model.User;
import pl.cubesoft.tigerspiketest.provider.flickr.model.CommentsResult;

/**
 * Created by CUBESOFT on 19.09.2017.
 */

class CommentPoolImpl implements CommentPool {
    private final CommentsResult commentsResult;
    private final CommentImpl commentImpl;

    static class CommentImpl implements Comment {

        private CommentsResult.Comment comment;

        CommentImpl(CommentsResult.Comment comment) {
            this.comment = comment;
        }
        CommentImpl() {
        }

        @Override
        public String getId() {
            return comment.getId();
        }

        @Override
        public String getBody() {
            return comment.getContent();
        }

        @Override
        public Date getCreatedAt() {
            return comment.getDateCreate();
        }

        @Override
        public User getCreator() {
            return null;
        }

        @Override
        public String getCreatorId() {
            return comment.getAuthor();
        }

        @Override
        public String getCreatorName() {
            return comment.getAuthorName();
        }

        public void setComment(CommentsResult.Comment comment) {
            this.comment = comment;
        }
    }

    public CommentPoolImpl(CommentsResult commentsResult) {
        this.commentsResult = commentsResult;
        this.commentImpl = new CommentImpl();
    }

    @Override
    public int getItemCount() {
        return commentsResult.getComments().getComments().size();
    }

    @Override
    public int getTotalItemCount() {
        return commentsResult.getComments().getComments().size();
    }

    @Override
    public Comment getItem(int position) {
        commentImpl.setComment(commentsResult.getComments().getComments().get(position));

        return commentImpl;
    }

    @Override
    public boolean canComment() {
        return true;
    }

    @Override
    public void addItem(Comment comment) {

        commentsResult.getComments().getComments().add(((CommentImpl) comment).comment);
    }
}
