package ca.qc.cvm.dba.memos.app;

import java.util.List;
import java.util.Observer;

import ca.qc.cvm.dba.memos.entity.Category;
import ca.qc.cvm.dba.memos.entity.Memo;
import ca.qc.cvm.dba.memos.event.CommonEvent;

public class Facade {
	private static Facade instance;
	
	private MngApplication app;
	
	private Facade() {
		app = new MngApplication();
	}
	
	public static Facade getInstance() {
		if (instance == null) {
			instance = new Facade();
		}
		
		return instance;
	}
	
	public void processEvent(CommonEvent event) {
		app.addEvent(event);
        new Thread(app).start();
	}
	
	public void addObserverClass(Observer o) {
		app.addObserver(o);
	}
	
	public List<Category> getCategoryList() {
		return app.getCategoryList();
	}
	
	public List<Memo> getMemoList() {
		return app.getMemoList();
	}
	
	public void testConnection() {
		app.testConnection();
	}
	
	public void exit() {
		app.exit();
	}
}
