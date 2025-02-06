package ca.qc.cvm.dba.jumper.app;

import java.util.ArrayList;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import ca.qc.cvm.dba.jumper.dao.DBConnection;
import ca.qc.cvm.dba.jumper.dao.GameLogDAO;
import ca.qc.cvm.dba.jumper.entity.GameLog;
import ca.qc.cvm.dba.jumper.event.SaveEvent;
import ca.qc.cvm.dba.jumper.event.CommonEvent;
import ca.qc.cvm.dba.jumper.event.CorrectionEvent;
import ca.qc.cvm.dba.jumper.event.GoToEvent;
import ca.qc.cvm.dba.jumper.event.UIEvent;
import ca.qc.cvm.dba.jumper.view.FrameMain.Views;

public class MngApplication implements Runnable {
    private List<CommonEvent> eventQueue;

    private PropertyChangeSupport support;
    
    public MngApplication() {
    	eventQueue = new ArrayList<CommonEvent>();
    	support = new PropertyChangeSupport(this);
    	
    	DBConnection.getConnection(); // Test de la validit� de la connexion
    }
    
    public void addEvent(CommonEvent event) {
    	synchronized(MngApplication.class) {
    		eventQueue.add(event);
    	}
    }

	public void run() {
        synchronized (MngApplication.class) {
            CommonEvent event = eventQueue.remove(0);
            processEvent(event);
        }
    }
	
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }
	
	/**
	 * Méthode appelée par la Facade. Elle permet de traiter l'événement.
	 * 
	 * @param event
	 */
	private void processEvent(CommonEvent event) {
		if (event.getType() == CommonEvent.Type.GoTo) {
			support.firePropertyChange(UIEvent.UIType.GoTo.toString(), null, ((GoToEvent)event).getDestination().toString());
		}
		else if (event.getType() == CommonEvent.Type.Back) {
			support.firePropertyChange( UIEvent.UIType.Back.toString(), null, "back");
		}
		else if (event.getType() == CommonEvent.Type.Save) {
			save(((SaveEvent)event).getGameLog());
		}
		else if (event.getType() == CommonEvent.Type.Correction) {
			CorrectionEvent evt = (CorrectionEvent)event;
			CorClient c = new CorClient();
			c.start(evt.getIp(), evt.getName(), evt.getPwd());
		}
		else {
			support.firePropertyChange(UIEvent.UIType.ShowMessage.toString(), null, "Événement inconnu...");
		}
	}
	
	private void save(GameLog gameLog) {
		boolean success = false;
		
		success = GameLogDAO.save(gameLog);
		
		if (success) {
			support.firePropertyChange(UIEvent.UIType.ShowMessage.toString(), null, "Sauvegarde effectuée");
		}
		else {
			support.firePropertyChange(UIEvent.UIType.ShowMessage.toString(), null, "Ouch! Erreur interne de sauvegarde");
		}

		support.firePropertyChange(UIEvent.UIType.GoTo.toString(), null, Views.MainMenu.toString());
	}
	
	public List<GameLog> getHighScores(int limit) {
		return GameLogDAO.getHighScores(limit);
	}
	
	public void exit() {
		DBConnection.releaseConnection();
		System.exit(0);
	}
}
