package org.example.data.repos;

import org.example.data.DatabaseSession;
import org.example.data.entities.Auction;
import org.example.data.entities.Tag;
import org.hibernate.Session;

import java.util.List;

import static org.example.data.DatabaseSession.sessionFactory;

public class TagsDbRepository implements TagsRepository {
    private static TagsDbRepository instance;

    private TagsDbRepository() {}

    public static TagsDbRepository getInstance() {
        if (instance == null) {
            instance = new TagsDbRepository();
        }
        return instance;
    }

    @Override
    public List<Tag> getAllTags() {
        return sessionFactory.openSession()
                .createQuery("FROM Tag", Tag.class).getResultList();
    }

    @Override
    public Tag addTag(Tag tag) {
        sessionFactory.inTransaction(session -> {
            if (isNewTag(session, tag))
                session.persist(tag);
        });
        return tag;
    }

    private boolean isNewTag(Session session, Tag tag) {
        return session.
                createSelectionQuery("FROM Tag WHERE tagName = :tagName", Tag.class)
                .setParameter("tagName", tag.getTagName()).getSingleResultOrNull() == null;
    }

    @Override
    public List<Tag> getTagsByAuction(Auction auction) {
        return DatabaseSession.getSession()
                .createSelectionQuery("SELECT tags FROM Auction WHERE id = :id", Tag.class)
                .setParameter("id", auction.getId()).getResultList();
    }
}
