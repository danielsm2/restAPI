package api.keystone;

public class Authentication {
	private Identity identity;
	
	public Authentication(Identity identity) {
		this.identity = identity;
	}

	public Identity getIdentity() {
		return identity;
	}

	public void setIdentity(Identity identity) {
		this.identity = identity;
	}
}
