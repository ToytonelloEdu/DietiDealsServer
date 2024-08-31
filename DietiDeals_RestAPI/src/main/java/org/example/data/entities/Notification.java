package org.example.data.entities;

import jakarta.persistence.*;
import org.example.data.entities.enums.NotificationType;

import java.time.LocalDateTime;
import java.util.*;

@Entity(name = "notification")
public class Notification {
    @Id @GeneratedValue
    private int id;

    @ManyToOne(optional = false)
    private Auction auction;

    @ManyToOne(optional = false)
    private User users;

    @Enumerated(EnumType.STRING)
    @Basic(optional = false)
    private NotificationType notificationType;

    @Basic(optional = false)
    private LocalDateTime time;

    @Basic(optional = false)
    private Boolean received = false;

    @Column(nullable = false)
    private Boolean read = false;

    public Notification() {}

    public Notification(Auction auction, User users, NotificationType notificationType, LocalDateTime time) {
        this.auction = auction;
        this.users = users;
        this.notificationType = notificationType;
        this.time = time;
    }

    public static List<Notification> auctionNotifications(Auction auction) {
        if (auction != null) {
            if (auction instanceof IncrementalAuction) return incrementalNotifications((IncrementalAuction) auction);
            else if (auction instanceof SilentAuction) return Collections.emptyList();
            else throw new IllegalArgumentException("Auction type not supported");
        }
        throw new IllegalArgumentException("auction is null");
    }

    private static List<Notification> incrementalNotifications(IncrementalAuction auction) {
        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification(auction, auction.getAuctioneer(), NotificationType.OWNER, LocalDateTime.now()));

        Bid winner = auction.retrieveLastBid();
        notifications.add(new Notification(auction, winner.getBuyer(), NotificationType.WINNER, LocalDateTime.now()));
        auction.getBids().remove(winner);

        Map<String, Bid> map = new HashMap<>();
        for(Bid bid : auction.getBids()) {
            map.put(bid.getBuyer().getUsername(), bid);
        }
        for(Bid bid : map.values()) {
            notifications.add(new Notification(auction, bid.getBuyer(), NotificationType.PARTICIPANT, LocalDateTime.now()));
        }

        return notifications;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    public User getUsers() {
        return users;
    }

    public void setUsers(User users) {
        this.users = users;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType message) {
        this.notificationType = message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Boolean getReceived() {
        return received;
    }

    public void setReceived(Boolean received) {
        this.received = received;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }
}
