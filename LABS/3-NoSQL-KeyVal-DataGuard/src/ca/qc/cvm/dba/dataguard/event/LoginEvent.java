package ca.qc.cvm.dba.dataguard.event;

public class LoginEvent extends CommonEvent {
	private char[] password;

	public LoginEvent(char[] password) {
		super(CommonEvent.Type.Login);
		
		this.password = password;
	}
	
	public char[] getPassword() {
		return password;
	}
}
