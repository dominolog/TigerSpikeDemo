package pl.cubesoft.tigerspiketest.data.model;

/**
 * Created by CUBESOFT on 16.09.2017.
 */

public interface Group {

    int getItemCount();
    GroupElement getItem(int position);

    String getTitle();

    String getId();
}
