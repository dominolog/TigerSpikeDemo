package pl.cubesoft.tigerspiketest.provider.flickr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by CUBESOFT on 14.09.2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class PhotoSetPhotosResult {

    private static final String format = "jpg";
    public class PhotoSet {

        @JsonProperty("id")
        Long id;

        @JsonProperty("primary")
        String primary;


        @JsonProperty("owner")
        String owner;

        @JsonProperty("ownername")
        String ownername;



        @JsonProperty("page")
        int page;

        @JsonProperty("pages")
        int pages;

        @JsonProperty("per_page")
        int perpage;

        @JsonProperty("perpage")
        int perpage2;

        @JsonProperty("total")
        int total;

        @JsonProperty("title")
        String title;

        @JsonProperty("photo")
        List<Photo> photo;

        public Long getId() {
            return id;
        }

        public String getPrimary() {
            return primary;
        }

        public int getPage() {
            return page;
        }

        public int getPages() {
            return pages;
        }

        public int getPerpage() {
            return perpage;
        }

        public int getTotal() {
            return total;
        }

        public String getTitle() {
            return title;
        }

        public List<Photo> getPhoto() {
            return photo;
        }

        public String getOwner() {
            return owner;
        }

        public String getOwnername() {
            return ownername;
        }
    }

    @JsonProperty("photoset")
    PhotoSet photoset;

    @JsonProperty("stat")
    String stat;

    public PhotoSet getPhotoset() {
        return photoset;
    }

    public String getStat() {
        return stat;
    }
}
