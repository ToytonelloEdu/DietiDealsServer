package org.example.data.repos;

import org.example.data.entities.Auction;
import org.example.data.entities.AuctionPhoto;

import java.util.List;

public interface PhotosRepository {
    void saveProfilePicture(String username, String path);

    void saveAuctionPicture(Integer auctionId, String path);

    List<AuctionPhoto> getPhotosByAuction(Auction auction);
}
