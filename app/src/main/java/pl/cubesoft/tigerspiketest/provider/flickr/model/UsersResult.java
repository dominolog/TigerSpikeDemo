package pl.cubesoft.tigerspiketest.provider.flickr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by CUBESOFT on 19.09.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsersResult {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Contact {
        @JsonProperty("nsid")
        String nsid;

        @JsonProperty("username")
        String username;

        @JsonProperty("iconserver")
        String iconserver;

        @JsonProperty("iconfarm")
        int iconfarm;

        @JsonProperty("ignored")
        int ignored;

        @JsonProperty("rev_ignored")
        int revIgnored;

        public String getNsid() {
            return nsid;
        }

        public String getUsername() {
            return username;
        }

        public String getIconserver() {
            return iconserver;
        }

        public int getIconfarm() {
            return iconfarm;
        }

        public int getIgnored() {
            return ignored;
        }

        public int getRevIgnored() {
            return revIgnored;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Contacts {
        @JsonProperty("page")
        int page;

        @JsonProperty("pages")
        int pages;

        @JsonProperty("perpage")
        int perpage;

        @JsonProperty("total")
        int total;


        @JsonProperty("contact")
        List<Contact> contact;

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

        public List<Contact> getContact() {
            return contact;
        }
    }

    @JsonProperty("contacts")
    Contacts contacts;

    public Contacts getContacts() {
        return contacts;
    }
}
