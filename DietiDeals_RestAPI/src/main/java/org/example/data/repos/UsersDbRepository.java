package org.example.data.repos;

import org.example.auth.AuthCredentials;
import org.example.data.DatabaseSession;
import org.example.data.entities.Auctioneer;
import org.example.data.entities.Buyer;
import org.example.data.entities.Links;
import org.example.data.entities.User;

public class UsersDbRepository implements UsersRepository {
    private static UsersDbRepository instance;
    private static AuctionsRepository auctionsRepo;
    private static BidsRepository bidsRepo;

    private UsersDbRepository() {
        PhotosDbRepository.getInstance(this);
        auctionsRepo = AuctionsDbRepository.getInstance();
        bidsRepo = BidsDbRepository.getInstance();
    }

    public static UsersDbRepository getInstance() {
        if (instance == null) {
            instance = new UsersDbRepository();
        }
        return instance;
    }

    @Override
    public User getUserByUsername(String username) {
        User subUser = DatabaseSession.getSession().find(User.class, username);
        System.out.println("\nSELECT * FROM users WHERE username = ?\n");
        if(subUser == null) return null;
        if(subUser.getLinks() != null) subUser.getLinks().setUser(null);

        if(subUser.getUserType().equals("Auctioneer")) {
            Auctioneer auctioneer = new Auctioneer(subUser);
            auctioneer.setAuctions(auctionsRepo.getAuctionsByAuctioneer(auctioneer));
            return auctioneer;
        } else {
            Buyer buyer = new Buyer(subUser);
            buyer.setBids(bidsRepo.getBidsByUser(buyer));
            return buyer;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        User user = DatabaseSession.getSession()
                .createSelectionQuery("FROM Users WHERE email = :email", User.class)
                .setParameter("email", email).getSingleResultOrNull();
        if(user == null) return null;
        if(user.getLinks() != null) user.getLinks().setUser(null);

        System.out.println("\nSELECT * FROM users WHERE email = ?\n");
        if(user instanceof Auctioneer) {
            Auctioneer auctioneer = (Auctioneer) user;
            auctioneer.setAuctions(auctionsRepo.getAuctionsByAuctioneer(auctioneer));
            return auctioneer;
        } else if (user instanceof Buyer) {
            Buyer buyer = (Buyer) user;
            buyer.setBids(bidsRepo.getBidsByUser(buyer));
            return buyer;
        }
        return user;
    }

    @Override
    public User getUserByHandle(String handle) {
        if (handle.contains("@")) {
            return getUserByEmail(handle);
        } else {
            return getUserByUsername(handle);
        }
    }


    @Override
    public User addUser(User user) {
        User subUser;
        try {
            if(user.getUserType().equals("Auctioneer")) {
                subUser = new Auctioneer(user);
            } else if(user.getUserType().equals("Buyer")) {
                subUser = new Buyer(user);
            } else throw new IllegalArgumentException("Invalid user type");

            DatabaseSession.inTransaction(session -> session.persist(subUser));

            System.out.println("\nINSERT (?, ?, ...) INTO users\n");
            return user;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }

    }

    @Override
    public User updateUser(User user) {
        if(user == null) return null;
        try {
            Links links = user.getLinks();
            if(links != null) {
                links.setUser(user);
                user.setLinks(links);
            }

            DatabaseSession.inTransaction(session -> session.merge(user));

            System.out.println("\nUPDATE (?, ?, ...) INTO users WHERE username = ?\n");
            if(links != null) links.setUser(null);
            return user;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }


    @Override
    public Boolean verifyCredentials(AuthCredentials auth) {
        User user = getUserByHandle(auth.getHandle());
        if(user != null)
            return user.checkCredentials(auth);
        else
            return false;
    }


    private UsersDbRepository(AuctionsRepository auctionsRepo, BidsRepository bidsRepo) {
        UsersDbRepository.auctionsRepo = auctionsRepo;
        UsersDbRepository.bidsRepo = bidsRepo;
    }

    public static UsersDbRepository getInstance(AuctionsRepository auctionsRepo, BidsRepository bidsRepo){
        if (instance == null) {
            instance = new UsersDbRepository(auctionsRepo, bidsRepo);
        } else
        {
            if(UsersDbRepository.auctionsRepo.getClass() == AuctionsDbRepository.class){
                throw new IllegalArgumentException("This Repository was already built with default dependencies");
            }
            if(UsersDbRepository.bidsRepo.getClass() == BidsDbRepository.class){
                throw new IllegalArgumentException("This Repository was already built with default dependencies");
            }
        }
        return instance;

    }
}
