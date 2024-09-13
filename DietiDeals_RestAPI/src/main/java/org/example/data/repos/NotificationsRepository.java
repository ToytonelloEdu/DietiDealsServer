package org.example.data.repos;

import org.example.data.entities.Notification;
import org.example.data.entities.User;

import java.util.List;

public interface NotificationsRepository {


    List<Notification> getNotificationsByUser(String handle);
}
