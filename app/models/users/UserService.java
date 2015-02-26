package models.users;

/**
 * Copyright 2013 Martin Hopkins
 *    based on InMemoryUserService by Jorge Aliss (jaliss at gmail dot com) - twitter: @jaliss
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import securesocial.core.Identity;
import securesocial.core.IdentityId;
import securesocial.core.OAuth1Info;
import securesocial.core.OAuth2Info;
import securesocial.core.PasswordInfo;
import securesocial.core.java.BaseUserService;
import securesocial.core.java.Token;

import java.util.HashMap;

import models.users.User;

import org.joda.time.DateTime;

import play.Application;

public class UserService extends BaseUserService {

    private HashMap<String, Token> tokens = new HashMap<String, Token>();

    public UserService(Application application) {
        super(application);
    }

    @Override
    public Identity doSave(Identity user) {
        
    	User mu = User.findByEmail(user.email().get());
        if (null == mu) {
            mu = new User();
            System.out.println("Register first name: "+user.firstName());
            
            //mu.setType(UserType.Retailer);
        }  
         mu.setAuthMethod(user.authMethod().method());
        if (user.avatarUrl().isDefined())
            mu.setAvatarUrl(user.avatarUrl().get());
        
        if (user.email().isDefined())
            mu.setEmail(user.email().get());
        
        if (null != user.oAuth1Info() && user.oAuth1Info().isDefined()) {
            OAuth1Info oAuth1 = user.oAuth1Info().get();
            if (null != oAuth1) {
                mu.setToken(oAuth1.token());
                mu.setSecret(oAuth1.secret());
            }
        }
        if (null != user.oAuth2Info() && user.oAuth2Info().isDefined()) {
            OAuth2Info oAuth2 = user.oAuth2Info().get();
            if (null != oAuth2) {
                mu.setAccessToken(oAuth2.accessToken());
                if (oAuth2.tokenType().isDefined())
                    mu.setTokenType(oAuth2.tokenType().get());
                mu.setExpiresIn((Integer ) oAuth2.expiresIn().get());
                if (oAuth2.refreshToken().isDefined())
                    mu.setRefreshToken(oAuth2.refreshToken().get());
            }
        }
    //    mu.setDisplayName(user.fullName());
       mu.setFirstName(user.firstName());
    // Jack - I commented out the following line since it looks wrong...
        //mu.setType(UserType.Retailer);
        mu.setName(user.fullName());
        mu.setUserId(user.identityId().userId());
       // mu.setLastName(user.lastName());
        mu.setProviderId(user.identityId().providerId());
       
        if (null != user.passwordInfo() && user.passwordInfo().isDefined()) {
            PasswordInfo pwd = user.passwordInfo().get();
            if (null != pwd) {
                if (null != pwd.salt() && pwd.salt().isDefined())
                    mu.setSalt(pwd.salt().get());
                mu.setHasher(pwd.hasher());
                mu.setPassword(pwd.password());
            }
        }
        User.addUser(mu);
        return user;
    }
 

    @Override
    public void doSave(Token token) {
        MongoToken mt = new MongoToken();
        mt.email = token.email;
        mt.expirationTime = token.expirationTime.getMillis();
        mt.isSignUp = token.isSignUp;
        mt.uuid = token.uuid;
        mt.creationTime = token.creationTime.getMillis();
        MongoToken.save(mt);
    }

    @Override
    public Identity doFind(IdentityId userId) {
        User mu = User.findByEmail(userId.userId());
        if (null == mu) return null;
        return getUserIdentity(mu);
    }
    
    @Override
    public Token doFindToken(String tokenId) {
        MongoToken mt = MongoToken.find().filter("uuid", tokenId).get();
        if (null == mt) 
            return null;
        return getToken(mt);
    }
    
    @Override
    public Identity doFindByEmailAndProvider(String email, String providerId) {
        User mu = User.findByEmail(email);
        if (null == mu) return null;
       
        
        return getUserIdentity(mu);
    }

    @Override
    public void doDeleteToken(String uuid) {
        tokens.remove(uuid);
    }

    @Override
    public void doDeleteExpiredTokens() {
        if (null == MongoToken.find())
            return;                // MongoFactory Not configured yet...
        for (MongoToken mt : MongoToken.find().fetch()) {
            Token token = getToken(mt);
            if (token.isExpired() ) {
                MongoToken.delete(mt);
            }
        }
        //securesocial.signup.createAccount();
    }
    
    public static UserIdentity doFind(String userId) {
        User mu = User.findByEmail(userId);//.filter("userId",  userId)
                                      // .get();
        if (null == mu) return null;
        return getUserIdentity(mu);
    }
    
    public static UserIdentity getUserIdentity(User mu) {
        UserIdentity user = new UserIdentity();
        user.accessToken = mu.getAccessToken();
        user.authMethod = mu.getAuthMethod();
        user.avatarUrl = mu.getAvatarUrl();
        user.email = mu.getEmail();
        user.expiresIn = mu.getExpiresIn();
        user.firstName = mu.getFirstName();
       // user.firstName = mu.getDisplayName();
        user.hasher = mu.getHasher();
        user.idGivenByProvider = mu.getUserId();
        
        if (mu.getName() != null) {
        user.fullName = mu.getName();
        
        if (mu.getName().contains(" "))
        user.lastName = mu.getName().split(" ")[1];
        }
        user.password = mu.getPassword();
        user.providerId = mu.getProviderId();
        user.refreshToken = mu.getRefreshToken();
        user.salt = mu.getSalt();
        user.token = mu.getToken();
        user.tokenType = mu.getTokenType();
       
        return user;
    }

    private static Token getToken(MongoToken mt) {
        Token token = new Token();
        token.creationTime = new DateTime(mt.creationTime);
        token.email = mt.email;
        token.expirationTime = new DateTime(mt.expirationTime);
        token.isSignUp = mt.isSignUp;
        token.uuid = mt.uuid;
        return token;
    }

}
