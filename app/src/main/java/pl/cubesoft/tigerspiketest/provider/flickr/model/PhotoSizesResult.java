package pl.cubesoft.tigerspiketest.provider.flickr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by CUBESOFT on 06.10.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhotoSizesResult {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sizes {

        @JsonProperty("size")
        List<Size> size;

        public List<Size> getSize() {
            return size;
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Size {
        @JsonProperty("label")
        String label;

        @JsonProperty("width")
        int width;

        @JsonProperty("height")
        int height;


        @JsonProperty("source")
        String source;

        @JsonProperty("url")
        String url;

        @JsonProperty("media")
        String media;

        public String getLabel() {
            return label;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public String getSource() {
            return source;
        }

        public String getUrl() {
            return url;
        }

        public String getMedia() {
            return media;
        }
    }

    @JsonProperty("sizes")
    Sizes sizes;

    public Sizes getSizes() {
        return sizes;
    }
}
