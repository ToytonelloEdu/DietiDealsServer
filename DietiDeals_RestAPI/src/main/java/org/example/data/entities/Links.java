package org.example.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Links {
    @Id
    @OneToOne(optional = false)
    private User user;

    private String website;

    private String instagram;

    private String twitter;

    private String facebook;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @SuppressWarnings("unused")
    public String getWebsite() {
        return website;
    }

    @SuppressWarnings("unused")
    public void setWebsite(String website) {
        this.website = website;
    }

    @SuppressWarnings("unused")
    public String getInstagram() {
        return instagram;
    }

    @SuppressWarnings("unused")
    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    @SuppressWarnings("unused")
    public String getTwitter() {
        return twitter;
    }

    @SuppressWarnings("unused")
    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    @SuppressWarnings("unused")
    public String getFacebook() {
        return facebook;
    }

    @SuppressWarnings("unused")
    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }
}
