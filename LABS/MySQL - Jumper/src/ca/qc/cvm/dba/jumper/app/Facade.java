package ca.qc.cvm.dba.jumper.app;

import java.beans.PropertyChangeListener;
import java.util.List;

import ca.qc.cvm.dba.jumper.entity.GameLog;
import ca.qc.cvm.dba.jumper.event.CommonEvent;

/**
 * Cette classe est l'intermédiaire entre la logique et la vue
 * Entre les panel et le MngApplication. C'est le point d'entrée de la vue
 * vers la logique
 */
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
	
	public void addObserverClass( PropertyChangeListener pcl) {
		app.addPropertyChangeListener(pcl);
	}
	
	public List<GameLog> getHighScores(int limit) {
		return app.getHighScores(limit);
	}
			
	public void exit() {
		app.exit();
	}
}
