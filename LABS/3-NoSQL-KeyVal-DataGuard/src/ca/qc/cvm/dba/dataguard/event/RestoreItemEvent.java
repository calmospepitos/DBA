package ca.qc.cvm.dba.dataguard.event;

import java.io.File;

public class RestoreItemEvent extends CommonEvent {
	private String name;
	private File file;

	public RestoreItemEvent(String name, File file) {
		super(CommonEvent.Type.RestoreItem);
		
		this.file = file;
		this.name = name;
	}
	
	public File getFile() {
		return file;
	}
	
	public String getName() {
		return name;
	}
}
