package pl.cubesoft.tigerspiketest.provider.flickr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;

/**
 * Created by CUBESOFT on 14.09.2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class PhotosResult {


    public class Photos {
        @JsonProperty("page")
        int page;

        @JsonProperty("pages")
        int pages;

        int perpage;

        @JsonProperty("total")
        int total;

        @JsonProperty("stat")
        String stat;

        @JsonProperty("photo")
        List<Photo> photo;


        @JsonSetter("perpage")
        public void setPerPage(int perpage) {
            this.perpage = perpage;
        }

        @JsonSetter("per_page")
        public void setPerPage2(int perpage) {
            this.perpage = perpage;
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

        public String getStat() {
            return stat;
        }

        public List<Photo> getPhoto() {
            return photo;
        }
    }

    @JsonProperty("photos")
    Photos photos;


    public Photos getPhotos() {
        return photos;
    }
}
