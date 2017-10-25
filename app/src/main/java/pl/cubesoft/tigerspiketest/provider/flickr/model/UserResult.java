package pl.cubesoft.tigerspiketest.provider.flickr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by CUBESOFT on 14.09.2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResult {

    @JsonProperty("person")
    User person;

    @JsonProperty("stat")
    String stat;

    public User getPerson() {
        return person;
    }

    @Override
    public String toString() {
        return "User{" +
                "person=" + person +
                '}';
    }

    public String getStat() {
        return stat;
    }
}
