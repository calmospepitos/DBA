package ca.qc.cvm.dba.dataguard.event;

import java.io.File;

public class AddItemEvent extends CommonEvent {
	private File file;

	public AddItemEvent(File file) {
		super(CommonEvent.Type.AddItem);
		
		this.file = file;
	}
	
	public File getFile() {
		return file;
	}
}
