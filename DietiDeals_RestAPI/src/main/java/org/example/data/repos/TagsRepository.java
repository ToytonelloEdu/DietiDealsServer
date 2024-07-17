package org.example.data.repos;

import org.example.data.entities.Auction;
import org.example.data.entities.Tag;

import java.util.List;

public interface TagsRepository {
    List<Tag> getAllTags();

    Tag addTag(Tag tag);

    List<Tag> getTagsByAuction(Auction auction);
}
