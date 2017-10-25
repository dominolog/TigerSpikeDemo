package pl.cubesoft.tigerspiketest.provider.flickr.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by CUBESOFT on 14.09.2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {



    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Photos {
        @JsonProperty("firstdatetaken")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        Content<String> firstdatetaken;

        @JsonProperty("firstdate")
        Content<String> firstdate;

        @JsonProperty("count")
        Content<Integer> count;
    }

    @JsonProperty("id")
    String id;

    @JsonProperty("nsid")
    String nsid;

    @JsonProperty("ispro")
    Boolean isPro;

    @JsonProperty("iconserver")
    Integer iconServer;

    @JsonProperty("iconfarm")
    Integer iconFarm;

    @JsonProperty("username")
    Content<String> username;

    @JsonProperty("realname")
    Content<String> realname;

    @JsonProperty("location")
    Content<String> location;

    @JsonProperty("description")
    Content<String> description;

    @JsonProperty("photosurl")
    Content<String> photosurl;

    @JsonProperty("profileurl")
    Content<String> profileurl;

    @JsonProperty("mobileurl")
    Content<String> mobileurl;

    @JsonProperty("contact")
    Boolean contact;

    @JsonProperty("photos")
    Photos photos;

    public Boolean getContact() {
        return contact;
    }

    public void setContact(boolean contact) {
        this.contact = contact;
    }

    public String getId() {
        return id;
    }

    public String getNsid() {
        return nsid;
    }

    public Boolean getPro() {
        return isPro;
    }

    public Integer getIconServer() {
        return iconServer;
    }

    public Integer getIconFarm() {
        return iconFarm;
    }

    public String getUsername() {
        return username != null ? username.content : null;
    }

    public String getRealname() {
        return realname != null ? realname.content : null;
    }

    public String getLocation() {
        return location != null ? location.content : null;
    }

    public String getDescription() {
        return description != null ? description.content : null;
    }

    public String getPhotosurl() {
        return photosurl.content;
    }

    public String getProfileurl() {
        return profileurl.content;
    }

    public String getMobileurl() {
        return mobileurl.getContent();
    }

    public Photos getPhotos() {
        return photos;
    }
}
