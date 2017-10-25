package pl.cubesoft.tigerspiketest.provider.flickr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by CUBESOFT on 22.09.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfileResult {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Profile {
        @JsonProperty("id")
        String id;
        @JsonProperty("nsid")
        String nsid;
        @JsonProperty("join_date")
        Date joinDate;
        @JsonProperty("hometown")
        String hometown;
        @JsonProperty("showcase_set")
        String showcaseSet;

        @JsonProperty("website")
        String website;
        @JsonProperty("facebook")
        String facebook;
        @JsonProperty("twitter")
        String twitter;
        @JsonProperty("tumblr")
        String tumblr;

        @JsonProperty("instagram")
        String instagram;

        @JsonProperty("pinterest")
        String pinterest;


        public String getId() {
            return id;
        }

        public String getNsid() {
            return nsid;
        }

        public Date getJoinDate() {
            return joinDate;
        }

        public String getHometown() {
            return hometown;
        }

        public String getShowcaseSet() {
            return showcaseSet;
        }

        public String getWebsite() {
            return website;
        }

        public String getFacebook() {
            return facebook;
        }

        public String getTwitter() {
            return twitter;
        }

        public String getTumblr() {
            return tumblr;
        }

        public String getInstagram() {
            return instagram;
        }

        public String getPinterest() {
            return pinterest;
        }

        @Override
        public String toString() {
            return "Profile{" +
                    "id='" + id + '\'' +
                    ", nsid='" + nsid + '\'' +
                    ", joinDate=" + joinDate +
                    ", hometown='" + hometown + '\'' +
                    ", showcaseSet='" + showcaseSet + '\'' +
                    ", website='" + website + '\'' +
                    ", facebook='" + facebook + '\'' +
                    ", twitter='" + twitter + '\'' +
                    ", tumblr='" + tumblr + '\'' +
                    ", instagram='" + instagram + '\'' +
                    ", pinterest='" + pinterest + '\'' +
                    '}';
        }
    }
    @JsonProperty("profile")
    Profile profile;

    public Profile getProfile() {
        return profile;
    }

    @Override
    public String toString() {
        return "UserProfileResult{" +
                "profile=" + profile +
                '}';
    }
}
