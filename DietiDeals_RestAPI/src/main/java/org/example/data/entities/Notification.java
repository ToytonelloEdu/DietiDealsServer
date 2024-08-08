package org.example.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity(name = "notification")
public class Notification {
    @Id @GeneratedValue
    private int id;

    //TODO: Add rest of fields

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
