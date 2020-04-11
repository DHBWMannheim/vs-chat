package vs.chat.entities;

import java.util.Objects;

public class PasswordUser extends User {

	private static final long serialVersionUID = -7555607643359893934L;
	// private byte[] password;
	private int password;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(password);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PasswordUser other = (PasswordUser) obj;
		return password == other.password;
	}
	
	

}
