package api.keystone;

public class Password {
	
	private User user;
	
	public Password(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
