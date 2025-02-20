package ca.qc.cvm.dba.memos.entity;

import java.util.Date;

public class Memo {
	private int id;
	private String categoryName;
	private String text;
	private Date created;

	public Memo(int id, String categoryName, String text, Date created) {
		this.id = id;
		this.categoryName = categoryName;
		this.text = text;
		this.created = created;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String toString() {
		return String.format("[%s - %s] %s", created.toString(), categoryName, text);
	}
}
