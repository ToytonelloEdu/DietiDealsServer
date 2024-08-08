package org.example.data.repos;

import org.example.auth.AuthCredentials;
import org.example.data.entities.User;

public interface UsersRepository {

    User getUserByUsername(String username);

    User getUserByEmail(String username);

    User getUserByHandle(String handle);

    User addUser(User user);

    User updateUser(User user);

    Boolean verifyCredentials(AuthCredentials auth);
}
