package pl.cubesoft.tigerspiketest.provider.flickr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by CUBESOFT on 14.09.2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResult2 {

    @JsonProperty("user")
    User user;

    @JsonProperty("stat")
    String stat;

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "User{" +
                "user=" + user +
                '}';
    }

    public String getStat() {
        return stat;
    }
}
