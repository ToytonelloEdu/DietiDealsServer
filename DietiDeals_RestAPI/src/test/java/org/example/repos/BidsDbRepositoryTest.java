package org.example.repos;

import org.example.data.DatabaseSession;
import org.example.data.entities.*;
import org.example.data.repos.BidsDbRepository;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


class BidsDbRepositoryTest {

    static Auctioneer antonio = new Auctioneer(
            "antonioascione", "auctioneer", "antoascio@gmail.com", "test", "antonio", "ascione", "test", "Un ragazzo", "Italia", "Uomo", Timestamp.valueOf("2002-05-20 12:00:00"), null
    );

    static Auction astaLibro = new SilentAuction(
            0, "Il piccolo principe", "Testing", antonio, Timestamp.valueOf("2024-10-07 10:00:00"), "Blue", Timestamp.valueOf("2024-10-18 10:00:00"), null
    );

    static Buyer ciro = new Buyer(
            "ciroanastasio", "buyer", "ciroanastasio@gmail.com", "testing", "ciro", "anastasio", "test", "Un altro ragazzo", "Italia", "Uomo", Timestamp.valueOf("2002-11-18 15:00:00"), null
    );
    BidsDbRepository bidsDbRepository = BidsDbRepository.getInstance();

    @BeforeAll
    static void insertValues(){
        Session session = DatabaseSession.getSession();
        session.beginTransaction();
            session.persist(antonio);
        session.getTransaction().commit();

        session.beginTransaction();
            Auctioneer mergedAntonio = session.merge(antonio);
            astaLibro.setAuctioneer(mergedAntonio);
            session.persist(astaLibro);
        session.getTransaction().commit();

        session.beginTransaction();
            session.persist(ciro);
        session.getTransaction().commit();
    }

    @Test
    void testAddBid_ValidBid(){
        Bid offerta = new Bid(
                0, astaLibro, ciro, 5.00, Timestamp.valueOf("2024-10-09 11:30:20")
        );

        assertDoesNotThrow(() -> bidsDbRepository.addBid(offerta));
    }

    @Test
    void testAddBid_BidNull_throwsException(){
        assertThrows(IllegalArgumentException.class, () -> bidsDbRepository.addBid(null));
    }


    @Test
    void testAddBid_InvalidBidNullAuction_throwsException(){
        Bid offerta = new Bid(
                1, null, ciro, 2.00, Timestamp.valueOf("2024-10-09 11:30:00")
        );
        assertThrows(IllegalArgumentException.class, () -> bidsDbRepository.addBid(offerta));
    }

    Auction astaQuaderno = new SilentAuction(
            0, "Quaderno blu", "Testing", antonio, Timestamp.valueOf("2024-10-07 10:00:00"), "Blue", Timestamp.valueOf("2024-10-18 10:00:00"), null
    );
    @Test
    void testAddBid_AuctionNotPersisted_throwsException(){
        Bid offerta = new Bid(
                1, astaQuaderno, ciro, 2.00, Timestamp.valueOf("2024-10-09 11:30:00")
        );
        assertThrows(IllegalArgumentException.class, () -> bidsDbRepository.addBid(offerta));
    }


    @Test
    void testAddBid_InvalidBidNullBuyer_throwsException(){
        Bid offerta = new Bid(
                1, astaLibro, null, 2.00, Timestamp.valueOf("2024-10-09 11:30:00")
        );
        assertThrows(IllegalArgumentException.class, () -> bidsDbRepository.addBid(offerta));
    }

    Buyer matteo = new Buyer(
            "matteorossi", "buyer", "matteorossi@gmail.com", "testing", "matteo", "rossi", "test", "Un altro ragazzo", "Italia", "Uomo", Timestamp.valueOf("2002-11-18 15:00:00"), null
    );
    @Test
    void testAddBid_BuyerNotPersisted_throwsException(){
        Bid offerta = new Bid(
                1, astaQuaderno, matteo, 2.00, Timestamp.valueOf("2024-10-09 11:30:00")
        );
        assertThrows(IllegalArgumentException.class, () -> bidsDbRepository.addBid(offerta));
    }


    @Test
    void testAddBid_InvalidBidNullTime_throwsException(){
        Bid offerta = new Bid(
                1, null, ciro, 2.00, null
        );
        assertThrows(IllegalArgumentException.class, () -> bidsDbRepository.addBid(offerta));
    }

}
