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
public class PhotoSetsResult {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PhotoSetElement {


        @JsonProperty("id")
        String id;

        @JsonProperty("primary")
        String primary;

        @JsonProperty("secret")
        String secret;

        @JsonProperty("server")
        String server;

        @JsonProperty("farm")
        Integer farm;

        @JsonProperty("photos")
        Integer photos;

        @JsonProperty("videos")
        Integer videos;

        @JsonProperty("title")
        Content<String> title;

        @JsonProperty("description")
        Content<String> description;

        @JsonProperty("count_views")
        Integer countViews;

        @JsonProperty("count_comments")
        Integer countComments;

        @JsonProperty("can_comment")
        Boolean canComment;


        @JsonProperty("date_create")
        @JsonDeserialize(using = DateNumberDeserializer.class)
        Date dateCreate;

        @JsonProperty("date_update")
        @JsonDeserialize(using = DateNumberDeserializer.class)
        Date dateUpdate;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public class Photo {
            @JsonProperty("description")
            Content<String> description;

            @JsonProperty("datetaken")
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            Date dateTaken;

            @JsonProperty("ownername")
            String ownerName;

            @JsonProperty("iconserver")
            String iconServer;

            @JsonProperty("iconfarm")
            Integer iconFarm;

            @JsonProperty("views")
            Integer views;

            @JsonProperty("tags")
            String tags;

            @JsonProperty("media")
            String media;

            @JsonProperty("media_status")
            String mediaStatus;

            public Content<String> getDescription() {
                return description;
            }

            public Date getDateTaken() {
                return dateTaken;
            }

            public String getOwnerName() {
                return ownerName;
            }

            public String getIconServer() {
                return iconServer;
            }

            public Integer getIconFarm() {
                return iconFarm;
            }

            public Integer getViews() {
                return views;
            }

            public String getTags() {
                return tags;
            }

            public String getMedia() {
                return media;
            }

            public String getMediaStatus() {
                return mediaStatus;
            }
        }
        @JsonProperty("primary_photo_extras")
        Photo primaryPhoto;


        public String getId() {
            return id;
        }

        public String getPrimary() {
            return primary;
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

        public Integer getPhotos() {
            return photos;
        }

        public Integer getVideos() {
            return videos;
        }

        public Content<String> getTitle() {
            return title;
        }

        public Content<String> getDescription() {
            return description;
        }

        public Integer getCountViews() {
            return countViews;
        }

        public Integer getCountComments() {
            return countComments;
        }

        public Boolean getCanComment() {
            return canComment;
        }

        public Date getDateCreate() {
            return dateCreate;
        }

        public Date getDateUpdate() {
            return dateUpdate;
        }

        public Photo getPrimaryPhoto() {
            return primaryPhoto;
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PhotoSets {

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


        @JsonProperty("photoset")
        List<PhotoSetElement> photoSet;

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

        public List<PhotoSetElement> getPhotoSet() {
            return photoSet;
        }
    }

    @JsonProperty("photosets")
    PhotoSets photoSets;
    @JsonProperty("stat")
    String stat;

    public PhotoSets getPhotoSets() {
        return photoSets;
    }

    public String getStat() {
        return stat;
    }
}
