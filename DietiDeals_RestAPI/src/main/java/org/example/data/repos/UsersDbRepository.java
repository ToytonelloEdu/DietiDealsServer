package org.example.data.repos;

import org.example.auth.AuthCredentials;
import org.example.data.DatabaseSession;
import org.example.data.entities.Auctioneer;
import org.example.data.entities.Buyer;
import org.example.data.entities.User;

import static org.example.data.DatabaseSession.sessionFactory;

public class UsersDbRepository implements UsersRepository {
    private static UsersDbRepository instance;
    private static AuctionsRepository auctionsRepo;
    private static BidsRepository bidsRepo;

    private UsersDbRepository() {
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

        if(subUser == null) return null;

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
        return DatabaseSession.getSession()
                .createSelectionQuery("FROM Users WHERE email = :email", User.class)
                .setParameter("email", email).getSingleResultOrNull();
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
            sessionFactory.inTransaction(session -> {
                session.persist(subUser);
            });
            return user;
        } catch (Exception e) {
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
