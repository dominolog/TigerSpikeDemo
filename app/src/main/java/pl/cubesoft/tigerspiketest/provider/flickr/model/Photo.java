package pl.cubesoft.tigerspiketest.provider.flickr.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;
import java.util.List;

/**
 * Created by CUBESOFT on 14.09.2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Photo {

    @JsonProperty("id")
    long id;

    @JsonProperty("title")
    String title;

    @JsonProperty("owner")
    String owner;

    @JsonProperty("farm")
    int farm;

    @JsonProperty("server")
    String server;

    @JsonProperty("secret")
    String secret;


    @JsonProperty("description")
    Content<String> description;

    @JsonProperty("views")
    Integer views;

    @JsonProperty("dateupload")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date dateUpload;

    @JsonProperty("datetaken")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date dateTaken;

    @JsonProperty("tags")
    @JsonDeserialize(using = TagsDeserializer.class)
    List<String> tags;

    @JsonProperty("media")
    String media;

    @JsonProperty("o_width")
    Integer width;

    @JsonProperty("o_height")
    Integer height;

    public String getDescription() {
        return description != null ? description.content : null;
    }

    public Integer getViews() {
        return views;
    }

    public Date getDateUpload() {
        return dateUpload;
    }

    public Date getDateTaken() {
        return dateTaken;
    }

    public long getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getTitle() {
        return title;
    }

    public String getSecret() {
        return secret;
    }

    public int getFarm() {
        return farm;
    }

    public String getServer() {
        return server;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getMedia() {
        return media;
    }


    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }
}
