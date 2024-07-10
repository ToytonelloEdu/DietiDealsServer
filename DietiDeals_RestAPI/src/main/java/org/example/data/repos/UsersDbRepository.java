package org.example.data.repos;

import org.example.data.entities.User;

public class UsersDbRepository implements UsersRepository {
    private static UsersDbRepository instance;

    private UsersDbRepository() {}

    public static UsersDbRepository getInstance() {
        if (instance == null) {
            instance = new UsersDbRepository();
        }
        return instance;
    }

    @Override
    public User getUserByID(int id) {
        return null;
    }
}
