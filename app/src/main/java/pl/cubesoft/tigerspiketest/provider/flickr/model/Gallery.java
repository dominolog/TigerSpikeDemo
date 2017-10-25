package pl.cubesoft.tigerspiketest.provider.flickr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by CUBESOFT on 03.10.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Gallery {

    @JsonProperty("id")
    String id;

    @JsonProperty("url")
    String url;

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}
