package pl.cubesoft.tigerspiketest.provider.flickr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;
import java.util.List;

/**
 * Created by CUBESOFT on 19.09.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentsResult {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Comment {
        @JsonProperty("id")
        String id;

        @JsonProperty("author")
        String author;

        @JsonProperty("authorname")
        String authorName;

        @JsonProperty("iconserver")
        String iconServer;

        @JsonProperty("iconfarm")
        Integer iconfarm;

        @JsonProperty("datecreate")
        @JsonDeserialize(using = DateNumberDeserializer.class)
        Date dateCreate;

        @JsonProperty("permalink")
        String permalink;

        @JsonProperty("realname")
        String realname;

        @JsonProperty("_content")
        String content;

        public String getId() {
            return id;
        }

        public String getAuthor() {
            return author;
        }

        public String getAuthorName() {
            return authorName;
        }

        public String getIconServer() {
            return iconServer;
        }

        public Integer getIconfarm() {
            return iconfarm;
        }

        public Date getDateCreate() {
            return dateCreate;
        }

        public String getPermalink() {
            return permalink;
        }

        public String getRealname() {
            return realname;
        }

        public String getContent() {
            return content;
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Comments {
        @JsonProperty("photo_id")
        long photoId;

        @JsonProperty("comment")
        List<Comment> comments;

        public long getPhotoId() {
            return photoId;
        }

        public List<Comment> getComments() {
            return comments;
        }
    }
    @JsonProperty("comments")
    Comments comments;

    @JsonProperty("stat")
    String stat;

    public Comments getComments() {
        return comments;
    }

    public String getStat() {
        return stat;
    }
}
