package ca.qc.cvm.dba.memos.entity;

import java.util.Date;

public class User {
	private int id;
	private String username;
	
	public User(int id, String username) {
		this.id = id;
		this.username = username;
	}

	public int getId() {
		return id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String toString() {
		return id + "," + username;
	}
}
