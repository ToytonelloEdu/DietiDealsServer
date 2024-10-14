package org.example.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.List;

@Entity(name = "Tag")
public class Tag {

    @Id
    private String tagName;

    @ManyToMany(mappedBy = "tags")
    private List<Auction> auctions;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Tag() {}

    @SuppressWarnings("unused")
    public Tag(String tagName) {
        setTagName(tagName);
    }

    @Override
    public String toString() {
        return "Tag{" +
                ", tagName='" + tagName + '\'' +
                '}';
    }
}
