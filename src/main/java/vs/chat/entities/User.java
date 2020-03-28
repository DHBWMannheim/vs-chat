package vs.chat.entities;

import vs.chat.server.warehouse.Warehouseable;

import java.util.UUID;

public class User implements Comparable<User>, Warehouseable {

	private final UUID id;
	private String username;
	// private byte[] password;
	private int password; //TODO prevent this from being send but send -> new dto for id and username for loginsync

	public User() {
		this.id = UUID.randomUUID();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/*
	 * public String getPassword() { //return new String(password,
	 * Charset.defaultCharset()); }
	 */

	public boolean hasPassword(String password) {
		/*
		 * byte[] encodedPassword = this.encodePassword(password); for (int i = 0; i <
		 * encodedPassword.length; i++) { if (encodedPassword[i] != this.password[i]) {
		 * return false; } } return true;
		 */
		return this.password == password.hashCode();
	}

	public void setPassword(String password) {
		/*
		 * SecureRandom random = new SecureRandom(); byte[] salt = new byte[16];
		 * random.nextBytes(salt);
		 */
		this.password = this.encodePassword(password);
	}

	private int encodePassword(String password) {
		// Salt von environment
		/*
		 * KeySpec spec = new PBEKeySpec(password.toCharArray(), new byte[]{1, 2, 3, 4,
		 * 5}, 65536, 128); try { SecretKeyFactory factory =
		 * SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1"); return
		 * factory.generateSecret(spec).getEncoded(); } catch (NoSuchAlgorithmException
		 * | InvalidKeySpecException e) { e.printStackTrace(); } return new byte[0];
		 */
		return password.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return this.id.equals(obj);
	}

	@Override
	public UUID getId() {
		return id;
	}

	@Override
	public int compareTo(final User o) {
		return this.id.compareTo(o.id);
	}

}
