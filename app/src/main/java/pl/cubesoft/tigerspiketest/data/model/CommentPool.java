package pl.cubesoft.tigerspiketest.data.model;

/**
 * Created by CUBESOFT on 19.09.2017.
 */

public interface CommentPool {

    int getItemCount();
    int getTotalItemCount();

    Comment getItem(int position);


    boolean canComment();

    void addItem(Comment comment);
}
