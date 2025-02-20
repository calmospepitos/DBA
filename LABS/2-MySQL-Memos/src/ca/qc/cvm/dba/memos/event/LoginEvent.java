package ca.qc.cvm.dba.memos.event;

public class LoginEvent extends CommonEvent {
	private String username;
	private char[] password;

	public LoginEvent(String username, char[] password) {
		super(CommonEvent.Type.Login);
		
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public char[] getPassword() {
		return password;
	}
}
