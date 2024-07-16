package org.example.data.repos;

import org.example.auth.AuthCredentials;
import org.example.data.entities.User;

public interface UsersRepository {

    User getUserByUsername(String username);

    User addUser(User user);

    Boolean verifyCredentials(AuthCredentials auth);
}
