package pl.cubesoft.tigerspiketest.provider.flickr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by CUBESOFT on 27.09.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExifResult {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Exif {
        @JsonProperty("tagspace")
        String tagSpace;
        @JsonProperty("tagspaceid")
        long tagSpaceId;
        @JsonProperty("tag")
        String tag;
        @JsonProperty("label")
        String label;
        @JsonProperty("raw")
        Content<String> raw;

        public String getTagSpace() {
            return tagSpace;
        }

        public long getTagSpaceId() {
            return tagSpaceId;
        }

        public String getTag() {
            return tag;
        }

        public String getLabel() {
            return label;
        }

        public Content<String> getRaw() {
            return raw;
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Photo {
        @JsonProperty("id")
        String id;
        @JsonProperty("secret")
        String secret;
        @JsonProperty("server")
        String server;
        @JsonProperty("farm")
        Integer farm;
        @JsonProperty("camera")
        String camera;

        @JsonProperty("exif")
        List<Exif> exif;

        public String getId() {
            return id;
        }

        public String getSecret() {
            return secret;
        }

        public String getServer() {
            return server;
        }

        public Integer getFarm() {
            return farm;
        }

        public String getCamera() {
            return camera;
        }

        public List<Exif> getExif() {
            return exif;
        }
    }


    @JsonProperty("photo")
    Photo photo;

    public Photo getPhoto() {
        return photo;
    }
}

