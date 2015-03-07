/**
 * Copyright 2012-2014 Jorge Aliss (jaliss at gmail dot com) - twitter: @jaliss
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
package service;

import persistence.MongoDBManager;
import play.Logger;
import play.libs.F;
import securesocial.core.BasicProfile;
import securesocial.core.PasswordInfo;
import securesocial.core.services.SaveMode;
import securesocial.core.java.BaseUserService;
import securesocial.core.java.Token;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoURI;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import models.User;
import models.users.UserToken;

/**
 */
public class UserService extends BaseUserService<UserCredential> {
    public Logger.ALogger logger = play.Logger.of("application.service.UserService");

    @Override
    public F.Promise<UserCredential> doSave(BasicProfile profile, SaveMode mode) {
        UserCredential result = new UserCredential(profile);
        if (mode == SaveMode.SignUp()) {
        	
            User.save(result);
        } else if (mode == SaveMode.LoggedIn()) {
          //  User.update(result);
        } else if (mode == SaveMode.PasswordChange()) {
           // User.updatePassword(result);
        } else {
            throw new RuntimeException("Unknown mode");
        }
        return F.Promise.pure(result);
    }

    @Override
    public F.Promise<UserCredential> doLink(UserCredential current, BasicProfile to) {
        User target = User.findByEmail(current.main.userId());
        if ( target == null ) {
            // this should not happen
            throw new RuntimeException("Can't find user : " + current.main.userId());
        }
        // TODO - get linked credentials
//        boolean alreadyLinked = false;
//        for ( BasicProfile p : target.identities) {
//            if ( p.userId().equals(to.userId()) && p.providerId().equals(to.providerId())) {
//                alreadyLinked = true;
//                break;
//            }
//        }
//        if (!alreadyLinked) target.identities.add(to);
        return F.Promise.pure(target.getCredentials());
    }

    @Override
    public F.Promise<Token> doSaveToken(Token token) {
    /*	UserToken toke = new UserToken();
    	toke.createdAt = new Date(token.creationTime.getMillis());
    	toke.email = token.email;
    	toke.expireAt = new Date(token.expirationTime.getMillis());
    	toke.isSignUp = token.isSignUp;
    	toke.uuid = token.uuid;*/
    	UserToken.save(token);
        return F.Promise.pure(token);
    }
    public F.Promise<BasicProfile> doFind(String userId) {
    	
        if(logger.isDebugEnabled()){
            logger.debug("Finding user " + userId);
        }
        User localUser = User.findByEmail(userId);
         if (localUser == null) return F.Promise.pure(null);
          
        BasicProfile found = localUser.getProfile();
        return F.Promise.pure(found);
    }
    @Override
    public F.Promise<BasicProfile> doFind(String providerId, String userId) {
    	
        if(logger.isDebugEnabled()){
            logger.debug("Finding user " + userId);
        }
        User localUser = User.findByEmail(userId);
        if (localUser == null) return F.Promise.pure(null);
          
        BasicProfile found = localUser.getProfile();
        return F.Promise.pure(found);
    }

    @Override
    public F.Promise<PasswordInfo> doPasswordInfoFor(UserCredential user) {
        throw new RuntimeException("doPasswordInfoFor is not implemented yet in sample app");
    }

    @Override
    public F.Promise<BasicProfile> doUpdatePasswordInfo(UserCredential user, PasswordInfo info) {
        throw new RuntimeException("doUpdatePasswordInfo is not implemented yet in sample app");
    }

    @Override
    public F.Promise<Token> doFindToken(String tokenId) {
        UserToken userToken = UserToken.findbyId(tokenId);
        if (userToken == null) return F.Promise.pure(null);
        return F.Promise.pure(userToken.getToken());
    }

    @Override
    public F.Promise<BasicProfile> doFindByEmailAndProvider(String email, String providerId) {
    	User user = User.findByEmail(email);
    	if (null == user) return F.Promise.pure(null);
        BasicProfile profile = user.getProfile();
        return F.Promise.pure(profile);
    }

    @Override
    public F.Promise<Token> doDeleteToken(String uuid) {
        UserToken userToken = UserToken.findbyId(uuid);
        if (userToken == null) return F.Promise.pure(null);;
        userToken.delete();
        return F.Promise.pure(userToken.getToken());
    }

    @Override
    public void doDeleteExpiredTokens() {
        List<UserToken> list = MongoDBManager.getInstance().dataStore.createQuery(UserToken.class).filter("expireAt", "< "+System.currentTimeMillis()).asList();//UserToken.find.where().lt("expireAt", new DateTime().toString()).findList();
        for (UserToken UserToken : list) {
            UserToken.delete();
        }
    }
}
