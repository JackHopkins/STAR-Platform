package models.users;

import models.users.User;
import securesocial.core.Identity;
import securesocial.core.java.Authorization;
import service.UserCredential;

public class CheckAdminStatus implements Authorization<UserCredential> {
    public boolean isAuthorized(UserCredential userCredential, String params[]) {
        return User.getUserType(userCredential.main.email().get()).equals(UserType.Admin);
    }
}