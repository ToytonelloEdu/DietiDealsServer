package org.example.data.repos;

import org.example.data.entities.User;

public interface UsersRepository {

    User getUserByID(int id);

}
