package pl.cubesoft.tigerspiketest.provider.flickr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by CUBESOFT on 18.09.2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Content<T>{
    @JsonProperty("_content")
    T content;

    public T getContent() {
        return content;
    }




}