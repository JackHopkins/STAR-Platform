package models.users;

import securesocial.core.Identity;
import securesocial.core.java.Authorization;

public class CheckAdminStatus implements Authorization {
	    public boolean isAuthorized(Identity user, String params[]) {
	        return User.getUserType(user.email().get()).equals(UserType.Admin);
	    }
	}