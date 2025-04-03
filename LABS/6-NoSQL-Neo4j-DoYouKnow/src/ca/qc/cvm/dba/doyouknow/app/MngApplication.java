package ca.qc.cvm.dba.doyouknow.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import ca.qc.cvm.dba.doyouknow.dao.UserDAO;
import ca.qc.cvm.dba.doyouknow.event.ButtonEvent;
import ca.qc.cvm.dba.doyouknow.event.CommonEvent;
import ca.qc.cvm.dba.doyouknow.event.GeneralEvent;
import ca.qc.cvm.dba.doyouknow.event.GeneralEvent.General;
import ca.qc.cvm.dba.doyouknow.event.UIEvent;

public class MngApplication extends Observable implements Runnable {
    private List<CommonEvent> eventQueue;
	private UserDAO graph;
	private CorClient client;
    
    public MngApplication() {
    	eventQueue = new ArrayList<CommonEvent>();
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
	
	private void processEvent(CommonEvent event) {
		if (event.getType() == CommonEvent.Type.GENERAL_EVENT) {
			GeneralEvent evt = (GeneralEvent)event;

			setChanged();
			notifyObservers(new UIEvent(UIEvent.UIType.ShowMessage, "Veuillez patienter...", false));
			
			if (evt.getGeneralType() == General.LOAD_DATABASE) {
				openDatabase();
			}
		}
		else if (event.getType() == CommonEvent.Type.BUTTON_EVENT) {
			ButtonEvent evt = (ButtonEvent)event;
			
			List<String> results = new ArrayList<String>();
			
			setChanged();
			notifyObservers(new UIEvent(UIEvent.UIType.ShowMessage, "\nExécution du bouton #" + evt.getButtonNo() + "...", false));
			
			switch(evt.getButtonNo()) {
				case 1: 
					results = graph.findAllUsers();
					break;
				case 2:
					results = graph.getDirectConnectionsOf(evt.getText());
					break;
				case 3:
					results = graph.getPopularUsers();
					break;
				case 4:
					results = graph.proposeConnection(evt.getText());
					break;
				case 5:
					results = graph.checkUnconnected();
					break;
				case 6:
					results = graph.getOldest();
					break;
				case 10:
					results = null;
					client.start(evt.getText(), evt.getText2(), evt.getText3());
					break;
				default:
					break;
			}
			 
			if (results != null) {
				if (results.size() > 0) {
					for (String line : results) {
						setChanged();
				    	notifyObservers(new UIEvent(UIEvent.UIType.ShowMessage, "- " + line + "\n", true));
					}
				}
				else {
					setChanged();
			    	notifyObservers(new UIEvent(UIEvent.UIType.ShowMessage, "- Aucun résultat\n", true));
				}
			}
			
		}
	}
	
    public void openDatabase() {
    	graph = new UserDAO();
    	client = new CorClient(graph);
    	
    	setChanged();
    	notifyObservers(new UIEvent(UIEvent.UIType.ShowMessage, "Base de données ouverte.", true));
    }
    	
	public void exit() {
		System.exit(0);
	}
}
