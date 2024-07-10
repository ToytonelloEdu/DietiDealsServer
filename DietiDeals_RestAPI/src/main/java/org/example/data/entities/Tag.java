package org.example.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity(name = "Tag")
public class Tag {

    @Id @GeneratedValue
    private int id;

    private String tagName;


}
