package pl.cubesoft.tigerspiketest.provider.flickr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by CUBESOFT on 14.09.2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class PhotoInfo {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserInfo {

        @JsonProperty("nsid")
        String nsid;

        @JsonProperty("username")
        String username;

        @JsonProperty("realname")
        String realname;

        @JsonProperty("location")
        String location;

        @JsonProperty("iconserver")
        String iconserver;

        @JsonProperty("iconfarm")
        String iconfarm;

        @JsonProperty("path_alias")
        String path_alias;

        public String getNsid() {
            return nsid;
        }

        public String getUsername() {
            return username;
        }

        public String getRealname() {
            return realname;
        }

        public String getLocation() {
            return location;
        }

        public String getIconserver() {
            return iconserver;
        }

        public String getIconfarm() {
            return iconfarm;
        }

        public String getPath_alias() {
            return path_alias;
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Tags {
        @JsonProperty("tag")
        List<Tag> tag;

        public List<Tag> getTag() {
            return tag;
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Tag {

        @JsonProperty("id")
        String id;

        @JsonProperty("author")
        String author;

        @JsonProperty("authorname")
        String  authorname;

        @JsonProperty("raw")
        String  raw;

        @JsonProperty("_content")
        String  content;

        public String getId() {
            return id;
        }

        public String getAuthor() {
            return author;
        }

        public String getAuthorname() {
            return authorname;
        }

        public String getRaw() {
            return raw;
        }

        public String getContent() {
            return content;
        }
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Place {
        @JsonProperty("_content")
        String content;

        @JsonProperty("place_id")
        String placeId;

        @JsonProperty("woeid")
        String woeid;

        public String getContent() {
            return content;
        }

        public String getPlaceId() {
            return placeId;
        }

        public String getWoeid() {
            return woeid;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location {

        @JsonProperty("latitude")
        Double latitude;

        @JsonProperty("longitude")
        Double longitude;


        @JsonProperty("accuracy")
        Integer accuracy;

        @JsonProperty("locality")
        Place  locality;

        @JsonProperty("county")
        Place  county;

        @JsonProperty("region")
        Place  region;

        @JsonProperty("country")
        Place  country;

        public Double getLatitude() {
            return latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public Integer getAccuracy() {
            return accuracy;
        }

        public Place getLocality() {
            return locality;
        }

        public Place getCounty() {
            return county;
        }

        public Place getRegion() {
            return region;
        }

        public Place getCountry() {
            return country;
        }
    }

    @JsonProperty("id")
    long id;

    @JsonProperty("title")
    Content<String> title;

    @JsonProperty("owner")
    UserInfo owner;

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



    @JsonProperty("tags")
    Tags tags;

    @JsonProperty("media")
    String media;


    @JsonProperty("location")
    Location location;

    public long getId() {
        return id;
    }

    public Content<String> getTitle() {
        return title;
    }

    public UserInfo getOwner() {
        return owner;
    }

    public int getFarm() {
        return farm;
    }

    public String getServer() {
        return server;
    }

    public String getSecret() {
        return secret;
    }

    public Content<String> getDescription() {
        return description;
    }

    public Integer getViews() {
        return views;
    }

    public Tags getTags() {
        return tags;
    }

    public String getMedia() {
        return media;
    }

    public Location getLocation() {
        return location;
    }
}
