package ca.qc.cvm.dba.memos.event;

public class RegisterEvent extends CommonEvent {
	private String username;
	private char[] password;

	public RegisterEvent(String username, char[] password) {
		super(CommonEvent.Type.Register);
		
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
