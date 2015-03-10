package models;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Index;

import controllers.Application;
import scala.Some;
import securesocial.core.AuthenticationMethod;
//import models.Consumer;
//import models.User;
import securesocial.core.BasicProfile;
import securesocial.core.OAuth1Info;
import securesocial.core.OAuth2Info;
import securesocial.core.PasswordInfo;
import service.UserCredential;
import utils.BCrypt;
import models.track.Track;
import models.users.UserType;
import persistence.MongoDBManager;
import play.data.validation.Constraints.Required;


@Entity(value = "User", noClassnameStored = true)
@Indexes({
	@Index(value="userId", name="userIdIdx", unique=true),
	@Index(value="name", name="nameIdx", unique=false),
	@Index(value="email", name="emailIdx", unique=true),
})
public class User {

	@Property
	private Set<String> roles = new HashSet<String>();
	@Property @Required
	private UserType type;

	@Property @Required
	private boolean isMale;

	@Property 
	private boolean profileComplete = false;

	@Embedded
	private String email;

	@Property
	private String name;

	@Property
	private String avatarUrl;

	@Property
	private String userId;

	@Property
	private String providerId;

	@Property
	private String authMethod;

	@Property
	private long lastLogin;
	//@Property
	//	private String payPalEmail = null;
	//oAuth1Info
	@Property
	private String token;

	@Property
	private String secret;

	//oAuth2Info
	@Property
	private String accessToken;

	@Property
	private String tokenType;

	@Property
	private Integer expiresIn;

	@Property
	private String refreshToken;

	@Property
	private String hasher;

	@Property
	private String password;

	@Property
	private String firstName;

	@Property
	private long birthday;

	@Property
	private String address;


	public User() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String val) {
		providerId = val;
	}

	public String getAuthMethod() {
		return authMethod;
	}

	public void setAuthMethod(String authMethod) {
		this.authMethod = authMethod;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String val) {
		userId = val;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public Integer getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getHasher() {
		return hasher;
	}

	public void setHasher(String hasher) {
		this.hasher = hasher;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

//	public static void addUser(User user) {
//		if (MongoDBManager.getInstance().dataStore.createQuery(User.class).field("email").equal(user.getEmail()).get() == null) {
//
//			System.out.println("User: "+user.toString());
//			MongoDBManager.getInstance().dataStore.save(user);	
//			DigestFactory.addUser();
//		} else{
//			MongoDBManager.getInstance().update(user);
//		}
//	}

	public static User findByEmail(String email) {
		return MongoDBManager.getInstance().dataStore.createQuery(User.class).field("email").equal(email).get();
	}

	public static UserType getUserType(String email) {
		User user = findByEmail(email);

		if (user != null) return user.getType();
		return null;
	}
	
	public void addRole(String string) {
		roles.add(string);
	}
	public void removeRole(String string) {
		if (roles.contains(string))
			roles.remove(string);
	}
	public boolean hasRole(String string) {
		return roles.contains(string);
	}

	public boolean isMale() {
		return isMale;
	}

	public void setMale(boolean isMale) {
		this.isMale = isMale;
	}


	public boolean isProfileComplete() {
		return profileComplete;
	}

	public void setProfileComplete(boolean profileComplete) {
		this.profileComplete = profileComplete;
	}


	public static User createUser(String name, String firstName,
			String email, String password, String address, boolean male, UserType type) {

		User i = User.findByEmail(email);
		if (i == null) {
			i = new User();
		}
		i.setEmail(email);
		i.setMale(male);
		i.setFirstName(firstName);
		i.setName(name);
		i.setProfileComplete(true);
		i.setAddress(address);
		i.setProviderId("userpass");
		// Set up password using SecureSocial's default..BCrypt
		String salt = BCrypt.gensalt();
		String passwordHash = BCrypt.hashpw(password, salt);
	//	i.setSalt(salt);
		i.setPassword(passwordHash);
		i.setHasher("bcrypt");
		i.setAuthMethod("userpass");
		i.setType(type);

		return i;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getFirstName() {
		return firstName;
	}

	public String getAddress() {
		// TODO Auto-generated method stub
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public long getBirthday() {
		// TODO Auto-generated method stub
		return birthday;
	}
	public void setBirthday(long birthday) {
		this.birthday = birthday;
	}

	public long getLastLogin() {
		return lastLogin;
	}
	public Date getLastLoginDate() {
		return new Date(lastLogin);
	}
	public void setLastLogin(long lastLogin) {
		this.lastLogin = lastLogin;
	}

    public BasicProfile getProfile() {
		String hasher = "bcrypt";
	    String salt = BCrypt.gensalt();
    	PasswordInfo pwd = new PasswordInfo(hasher, password, new Some<String>(salt));
    	BasicProfile profile = new BasicProfile(providerId, 
			    								userId,
			    								new Some<String>(firstName),
			    								new Some<String>(name.substring(firstName.length())),
			    								new Some<String>(name),
			    								new Some<String>(email),
			    								new Some<String>(avatarUrl),
			    								AuthenticationMethod.UserPassword(),
			    								new Some<OAuth1Info>(null),
			    								new Some<OAuth2Info>(null),
			    								new Some<PasswordInfo>(pwd));
    	return profile;
    }
    
    
    public UserCredential getCredentials() {
    	BasicProfile profile = this.getProfile();
    	return new UserCredential(profile);
    }

    public static void save(UserCredential userCredential) {
    	String email = userCredential.main.email().get();
    	User user = User.findByEmail(email);
    	if (null != user) {
    		if (userCredential.main.avatarUrl().isDefined())
    			user.avatarUrl = userCredential.main.avatarUrl().get();
    		MongoDBManager.getInstance().dataStore.delete(user);
    	}else{
    		user = new User();
    		
    		user.authMethod = userCredential.main.authMethod().method();
    		if (userCredential.main.avatarUrl().isDefined())
    		user.avatarUrl = userCredential.main.avatarUrl().get();
    		user.email = userCredential.main.email().get();
    			
    				user.firstName = userCredential.main.firstName().get();
    				user.name = userCredential.main.fullName().get();
    				
    				//Maybe wrong?
    				user.profileComplete = false;
    				user.providerId = userCredential.main.providerId();
    				user.userId = userCredential.main.userId();
    				user.track = new Track();
    				
    				user.password = userCredential.main.passwordInfo().get().password();
    			
    				
    	}
    	MongoDBManager.getInstance().dataStore.save(user);
    	
	}


}
