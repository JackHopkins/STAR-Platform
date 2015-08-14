package models.users;

import java.util.Date;
import java.util.Iterator;

import org.joda.time.DateTime;

import securesocial.core.java.Token;

import org.mongodb.morphia.Key;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.query.Query;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;

@Entity
public class UserToken {

    private static final long serialVersionUID = 1L;

    @Id
    public String uuid;

    public String email;

    public Date createdAt;

    public Date expireAt;

    public boolean isSignUp;


	public Token getToken() {
		Token token = new Token();
		token.uuid = uuid;
		token.creationTime = new DateTime(createdAt);
		token.expirationTime = new DateTime(expireAt);
		token.isSignUp = isSignUp;
		token.email = email;
		return token;
	}

	public static void save(Token token) {
		UserToken userToken = new UserToken();
		userToken.createdAt = token.creationTime.toDate();
		userToken.expireAt = token.expirationTime.toDate();
		userToken.isSignUp = token.isSignUp;
		userToken.email = token.email;
		userToken.uuid = token.uuid;
	//	MongoDBManager.getInstance().dataStore.save(userToken);
	}

	public void delete() {
		//MongoDBManager.getInstance().dataStore.delete(this);
	}

	public static UserToken findbyId(String tokenId) {
		return null;//MongoDBManager.getInstance().dataStore.get(UserToken.class, tokenId);
	}
}