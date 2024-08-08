package org.example.data.repos;

import org.example.data.DatabaseSession;
import org.example.data.entities.Auction;
import org.example.data.entities.AuctionPhoto;
import org.example.data.entities.User;
import org.hibernate.Session;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PhotosDbRepository implements PhotosRepository {
    private static PhotosDbRepository instance;
    private static UsersRepository usersRepo;
    private static AuctionsRepository auctionsRepo;

    private PhotosDbRepository() {}

    public static PhotosDbRepository getInstance() {
        if (instance == null) {
            instance = new PhotosDbRepository();
        }
        return instance;
    }

    @Override
    public void saveProfilePicture(String username, String path) {
        if (usersRepo == null) usersRepo = UsersDbRepository.getInstance();


        User user = usersRepo.getUserByUsername(username);
        if (user == null) { throw new IllegalArgumentException("User not found"); }

        user.setProPicPath(path);
        if(usersRepo.updateUser(user) == null) {
            System.out.println("Picture path save has failed");
        }
    }

    @Override
    public void saveAuctionPicture(Integer auctionId, String path) {
        Session session = DatabaseSession.getSession();

            Auction auction = session.find(Auction.class, auctionId);
            if(auction == null) { throw new IllegalArgumentException("Auction not found");}

            session.beginTransaction();
            Auction mergedAuction = session.merge(auction);
            AuctionPhoto photo = new AuctionPhoto(
                    path, mergedAuction
            );
            session.persist(photo);
            session.getTransaction().commit();

            if(mergedAuction.getPictures().size() == 1)
                MedianColorCalculator.asyncMedianColorCalculation(mergedAuction.getId());
    }

    @Override
    public List<AuctionPhoto> getPhotosByAuction(Auction auction) {
        return DatabaseSession.getSession()
                .createSelectionQuery("SELECT new org.example.data.entities.AuctionPhoto(id, path) FROM AuctionPhoto " +
                        "WHERE auction = :auction ORDER BY id ASC", AuctionPhoto.class)
                .setParameter("auction", auction).getResultList();
    }

    private static class MedianColorCalculator{

        public static void asyncMedianColorCalculation(Integer auctionId) {
            Thread execution = new Thread(() -> {
                Session session = DatabaseSession.getSession();
                Auction auction = session.find(Auction.class, auctionId);

                String colorHex = averageColorHex(auction.firstPhoto().getPath());

                session.beginTransaction();
                session.evict(auction);
                auction.setMedianColor(colorHex);
                session.merge(auction);
                session.getTransaction().commit();
            });
            execution.start();
        }

        protected static String averageColorHex(String imagePath) {
            try {
                String path = "src/main/java/org/example/files/" + imagePath;
                BufferedImage image = ImageIO.read(new File(path));

                Color averageColor = getAverageColor(image);
                int r = averageColor.getRed();
                int g = averageColor.getGreen();
                int b = averageColor.getBlue();
                return String.format("FF%02X%02X%02X", r, g, b);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private static Color getAverageColor(BufferedImage image) {
            long sumRed = 0;
            long sumGreen = 0;
            long sumBlue = 0;
            int count = 0;

            int width = image.getWidth();
            int height = image.getHeight();

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Color pixelColor = new Color(image.getRGB(x, y));
                    sumRed += pixelColor.getRed();
                    sumGreen += pixelColor.getGreen();
                    sumBlue += pixelColor.getBlue();
                    count++;
                }
            }

            int avgRed = (int) (sumRed / count);
            int avgGreen = (int) (sumGreen / count);
            int avgBlue = (int) (sumBlue / count);

            return new Color(avgRed, avgGreen, avgBlue);
        }
    }
}
