package service;

import models.users.User;
import models.users.UserType;
import service.UserCredential;
import securesocial.core.java.Authorization;

public class AdminRole implements Authorization<UserCredential> {
    public boolean isAuthorized(UserCredential userCredential, String params[]) {
        return User.getUserType(userCredential.main.email().get()).equals(UserType.Admin);
    }
}