package ca.qc.cvm.dba.dataguard.app;

import java.util.List;
import java.util.Observer;

import ca.qc.cvm.dba.dataguard.entity.Item;
import ca.qc.cvm.dba.dataguard.event.CommonEvent;

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
	
	public void testConnection() {
		app.testConnection();
	}
	
	public List<Item> getItemList() {
		return app.getItemList();
	}
	
	public void exit() {
		app.exit();
	}
}
