package pl.cubesoft.tigerspiketest.provider.flickr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by CUBESOFT on 03.10.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GalleryResult {

    @JsonProperty("gallery")
    Gallery gallery;

    @JsonProperty("stat")
    String stat;

    public Gallery getGallery() {
        return gallery;
    }

    public String getStat() {
        return stat;
    }
}
