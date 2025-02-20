package ca.qc.cvm.dba.memos.entity;

public class Category {
	private int id;
	private int userId;
	private String name;

	public Category(int id, int userId, String name) {
		this.id = id;
		this.userId = userId;
		this.name = name;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
}
