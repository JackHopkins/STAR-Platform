package models.users;

import persistence.MongoDBManager;

public class User {

	private String authMethod;
	private String avatarUrl;
	private String email;
	private String token;
	private String secret;
	private String accessToken;
	private String tokenType;
	private Integer expiresIn;
	private String firstName;
	private String name;
	private String hasher;
	private String salt;
	private String userId;
	private String providerId;
	private String password;
	private String refreshToken;
	private UserType type;
	public User() {
		
	}
	public static User findByEmail(String string) {
		return MongoDBManager.getInstance().dataStore.createQuery(User.class).field("email").equal(string).get();
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getHasher() {
		return hasher;
	}

	public void setHasher(String hasher) {
		this.hasher = hasher;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Integer getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getAuthMethod() {
		return authMethod;
	}

	public void setAuthMethod(String authMethod) {
		this.authMethod = authMethod;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public static void addUser(User mu) {
		MongoDBManager.getInstance().dataStore.save(mu);
	}
	public static UserType getUserType(String email) {
		User user = findByEmail(email);

		if (user != null) return user.getType();
		return null;
	}
	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

}
