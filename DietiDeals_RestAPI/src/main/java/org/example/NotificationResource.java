package org.example;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.example.data.entities.Notification;
import org.example.data.repos.NotificationsDbRepository;
import org.example.data.repos.NotificationsRepository;

import java.util.List;

@Path("notifications")
public class NotificationResource {
    private final NotificationsRepository notificationsRepo;

    public NotificationResource() {
        notificationsRepo = NotificationsDbRepository.getInstance();
    }

    @GET
    @Path("{handle}")
    //@RequireAuth
    public List<Notification> getNotifications(@PathParam("handle") String handle) {
        try{
            return notificationsRepo.getNotificationsByUser(handle);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

}
