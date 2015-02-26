package models.users;


import scala.Option;
import securesocial.core.AuthenticationMethod;
import securesocial.core.Identity;
import securesocial.core.OAuth1Info;
import securesocial.core.OAuth2Info;
import securesocial.core.PasswordInfo;
import securesocial.core.IdentityId;
import securesocial.core.java.SecureSocial;

import play.mvc.Http.Context;

public class UserIdentity implements Identity {

    public String idGivenByProvider;
    public String providerId;

    public String firstName;
    public String lastName;
    public String fullName;
    public String email;
    public String avatarUrl;
    public String authMethod;

    //oAuth1Info
    public String token;
    public String secret;

    //oAuth2Info
    public String accessToken;
    public String tokenType;
    public Integer expiresIn;
    public String refreshToken;

    public String hasher;
    public String password;
    public String salt;
    
    @Override
    public AuthenticationMethod authMethod() {
        return new AuthenticationMethod(authMethod);
    }

    @Override
    public Option<String> avatarUrl() {
        return Option.apply(avatarUrl);
    }

    @Override
    public Option<String> email() {
        return Option.apply(email);
    }

    @Override
    public String firstName() {
        return firstName;
    }

    @Override
    public String fullName() {
        return fullName;
    }

    @Override
    public IdentityId identityId() {
        return new IdentityId(idGivenByProvider, providerId);
    }

    @Override
    public String lastName() {
        return lastName;
    }

    @Override
    public Option<OAuth1Info> oAuth1Info() {
        if (null != token && null != secret) {
            OAuth1Info info = new OAuth1Info(token, secret);
            return Option.apply(info);
        }
        return null;
    }

    @Override
    public Option<OAuth2Info> oAuth2Info() {
        if (null != accessToken && null != tokenType && null != expiresIn && null != refreshToken) {
            Option<String> token = Option.apply(tokenType);
            Option<Object> expires = Option.apply((Object) expiresIn);
            Option<String> refresh = Option.apply(refreshToken);
            OAuth2Info info = new OAuth2Info(accessToken, token, expires, refresh);
            return Option.apply(info);
        }
        return null;
    }

    @Override
    public Option<PasswordInfo> passwordInfo() {
        PasswordInfo info = new PasswordInfo(hasher, password, Option.apply(salt));
        return Option.apply(info);
    }
    
    public static UserIdentity getUserIdentity() {
        Context ctx = null;
        try {
            ctx = Context.current();
        } catch (RuntimeException e) {
            return null;
        }
        UserIdentity user = (UserIdentity) ctx.args.get(SecureSocial.USER_KEY);
     //   if (null == user) {
          //  if (Common.isDev())
            //    user = MongoUserService.doFind("buttash@gmail.com");
     //   }
        return user;
    }
    
    
}
