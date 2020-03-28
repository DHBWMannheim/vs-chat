package vs.chat.entities;

public class PasswordUser extends User {

	// private byte[] password;
	private int password; // TODO prevent this from being send but send -> new dto for id and username for
							// loginsync

	public PasswordUser() {
		super();
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

}
