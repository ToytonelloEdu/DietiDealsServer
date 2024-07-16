package org.example.data.repos;

import org.example.auth.AuthCredentials;
import org.example.data.entities.Auctioneer;
import org.example.data.entities.Bid;
import org.example.data.entities.Buyer;
import org.example.data.entities.User;

import static org.example.data.DatabaseSession.sessionFactory;

public class UsersDbRepository implements UsersRepository {
    private static UsersDbRepository instance;
    private static AuctionsRepository auctionsRepo;

    private UsersDbRepository() {
        auctionsRepo = AuctionsDbRepository.getInstance();
    }

    public static UsersDbRepository getInstance() {
        if (instance == null) {
            instance = new UsersDbRepository();
        }
        return instance;
    }

    @Override
    public User getUserByUsername(String username) {
        User subUser = sessionFactory.openSession().find(User.class, username);

        if(subUser.getUserType().equals("Auctioneer")) {
            Auctioneer auctioneer = new Auctioneer(subUser);
            auctioneer.setAuctions(auctionsRepo.getAuctionsByAuctioneer(auctioneer));
            return auctioneer;
        } else {
            Buyer buyer = new Buyer(subUser);
            buyer.setBids( //TODO: Implement in BidsRepository
                    sessionFactory.openSession()
                        .createSelectionQuery("FROM Bid WHERE buyer = :buyer", Bid.class)
                        .setParameter("buyer", buyer).getResultList()
            );
            return buyer;
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
        User user = sessionFactory.openSession().find(User.class, auth.getUsername());
        return user.checkCredentials(auth);
    }



    private UsersDbRepository(AuctionsRepository auctionsRepo) {
        UsersDbRepository.auctionsRepo = auctionsRepo;
    }

    public static UsersDbRepository getInstance(AuctionsRepository auctionsRepo){
        if (instance == null) {
            instance = new UsersDbRepository(auctionsRepo);
        } else
        if(UsersDbRepository.auctionsRepo.getClass() == AuctionsDbRepository.class){
            throw new IllegalArgumentException("This Repository was already built with default dependencies");
        }
        return instance;

    }
}
