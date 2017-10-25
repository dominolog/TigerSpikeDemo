package pl.cubesoft.tigerspiketest.data.model;

import java.util.Date;

/**
 * Created by CUBESOFT on 19.09.2017.
 */

public interface Comment {

    String getId();
    String getBody();
    Date getCreatedAt();


    User getCreator();
    String getCreatorId();
    String getCreatorName();

}
