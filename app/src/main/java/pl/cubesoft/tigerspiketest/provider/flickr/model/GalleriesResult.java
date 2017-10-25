package pl.cubesoft.tigerspiketest.provider.flickr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;
import java.util.List;

/**
 * Created by CUBESOFT on 14.09.2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class GalleriesResult {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GalleryElement {


        @JsonProperty("id")
        String id;

        @JsonProperty("url")
        String url;

        @JsonProperty("owner")
        String owner;

        @JsonProperty("username")
        String username;

        @JsonProperty("iconserver")
        String iconServer;

        @JsonProperty("iconfarm")
        Integer iconFarm;

        @JsonProperty("date_create")
        @JsonDeserialize(using = DateNumberDeserializer.class)
        Date dateCreate;

        @JsonProperty("date_update")
        @JsonDeserialize(using = DateNumberDeserializer.class)
        Date dateUpdate;

        @JsonProperty("count_photos")
        Integer countPhotos;

        @JsonProperty("count_videos")
        Integer countVideos;

        @JsonProperty("count_views")
        Integer countViews;

        @JsonProperty("count_comments")
        Integer countComments;

        @JsonProperty("title")
        Content<String> title;


        @JsonProperty("description")
        Content<String> description;

        @JsonProperty("primary_photo_id")
        long primaryPhotoId;

        @JsonProperty("primary_photo_server")
        String primaryPhotoServer;

        @JsonProperty("primary_photo_farm")
        String primaryPhotoFarm;

        @JsonProperty("primary_photo_secret")
        String primaryPhotoSecret;

        public String getId() {
            return id;
        }

        public String getUrl() {
            return url;
        }

        public String getOwner() {
            return owner;
        }

        public String getUsername() {
            return username;
        }

        public String getIconServer() {
            return iconServer;
        }

        public Integer getIconFarm() {
            return iconFarm;
        }

        public Date getDateCreate() {
            return dateCreate;
        }

        public Date getDateUpdate() {
            return dateUpdate;
        }

        public Integer getCountPhotos() {
            return countPhotos;
        }

        public Integer getCountVideos() {
            return countVideos;
        }

        public Integer getCountViews() {
            return countViews;
        }

        public Integer getCountComments() {
            return countComments;
        }

        public Content<String> getTitle() {
            return title;
        }

        public Content<String> getDescription() {
            return description;
        }

        public long getPrimaryPhotoId() {
            return primaryPhotoId;
        }

        public String getPrimaryPhotoServer() {
            return primaryPhotoServer;
        }

        public String getPrimaryPhotoFarm() {
            return primaryPhotoFarm;
        }

        public String getPrimaryPhotoSecret() {
            return primaryPhotoSecret;
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Galleries {

        @JsonProperty("total")
        int total;

        @JsonProperty("page")
        int page;

        @JsonProperty("pages")
        int pages;

        @JsonProperty("per_page")
        int perpage;

        @JsonProperty("user_id")
        String userId;


        @JsonProperty("gallery")
        List<GalleryElement> gallery;

        public int getTotal() {
            return total;
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

        public String getUserId() {
            return userId;
        }

        public List<GalleryElement> getGallery() {
            return gallery;
        }
    }

    @JsonProperty("galleries")
    Galleries galleries;
    @JsonProperty("stat")
    String stat;

    public Galleries getGalleries() {
        return galleries;
    }

    public String getStat() {
        return stat;
    }
}
