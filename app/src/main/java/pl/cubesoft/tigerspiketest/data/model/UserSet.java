package pl.cubesoft.tigerspiketest.data.model;


import pl.cubesoft.tigerspiketest.data.UserSetKey;

/**
 * Created by CUBESOFT on 13.09.2017.
 */

public interface UserSet {
    int getItemCount();
    int getTotalItemCount();
    User getItem(int position);

    UserSetKey getKey();
}
