package pl.cubesoft.tigerspiketest.provider.flickr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by CUBESOFT on 26.09.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhotoResult {

    @JsonProperty("photo")
    PhotoInfo photo;
    @JsonProperty("stat")
    String stat;

    public PhotoInfo getPhoto() {
        return photo;
    }

    public String getStat() {
        return stat;
    }
}
