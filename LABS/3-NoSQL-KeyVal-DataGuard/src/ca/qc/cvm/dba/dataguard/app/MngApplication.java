package ca.qc.cvm.dba.dataguard.app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import ca.qc.cvm.dba.dataguard.dao.DBConnection;
import ca.qc.cvm.dba.dataguard.dao.ItemDAO;
import ca.qc.cvm.dba.dataguard.dao.UserDAO;
import ca.qc.cvm.dba.dataguard.entity.Item;
import ca.qc.cvm.dba.dataguard.event.AddItemEvent;
import ca.qc.cvm.dba.dataguard.event.CommonEvent;
import ca.qc.cvm.dba.dataguard.event.DeleteItemEvent;
import ca.qc.cvm.dba.dataguard.event.GeneralEvent;
import ca.qc.cvm.dba.dataguard.event.LoginEvent;
import ca.qc.cvm.dba.dataguard.event.GoToEvent;
import ca.qc.cvm.dba.dataguard.event.RestoreItemEvent;
import ca.qc.cvm.dba.dataguard.event.UIEvent;
import ca.qc.cvm.dba.dataguard.event.GeneralEvent.GeneralType;

public class MngApplication extends Observable implements Runnable {
    private List<CommonEvent> eventQueue;
        
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
		if (event.getType() == CommonEvent.Type.Login) {
			LoginEvent evt = (LoginEvent)event;
			login(evt.getPassword());
		}
		else if (event.getType() == CommonEvent.Type.AddItem) {
			AddItemEvent evt = (AddItemEvent)event;
			addItem(evt.getFile());
		}
		else if (event.getType() == CommonEvent.Type.DeleteItem) {
			DeleteItemEvent evt = (DeleteItemEvent)event;
			deleteItem(evt.getName());
		}
		else if (event.getType() == CommonEvent.Type.RestoreItem) {
			RestoreItemEvent evt = (RestoreItemEvent)event;
			restoreItem(evt.getName(), evt.getFile());
		}
		else if (event.getType() == CommonEvent.Type.GoTo) {
			setChanged();
			notifyObservers(new UIEvent(UIEvent.UIType.GoTo, ((GoToEvent)event).getDestination().toString()));
		}
		else if (event.getType() == CommonEvent.Type.General) {
			GeneralEvent evt = (GeneralEvent) event;
			
			if (evt.getGeneralType() == GeneralType.Correct) {
				CorClient client = new CorClient();
				client.start(evt.getText(), evt.getText2(), evt.getText3());
			}
		}
	}
	
	public void testConnection() {
		boolean success = DBConnection.connectionCredentialsValid();
		
		if (!success) {
			setChanged();
			notifyObservers(new UIEvent(UIEvent.UIType.ShowMessage, "Informations de connexion invalides (impossible de se connecter à la BD"));
			setChanged();
			notifyObservers(new UIEvent(UIEvent.UIType.ForceExit, null));
		}
	}
	
	private void addItem(File file) {
		try {
			byte[] data = Files.readAllBytes(file.toPath());
			boolean success = ItemDAO.addItem(file.getName(), data);
			setChanged();

			if (success) {
				notifyObservers(new UIEvent(UIEvent.UIType.Refresh, null));
			}
			else {
				notifyObservers(new UIEvent(UIEvent.UIType.ShowMessage, "Erreur lors de l'insertion dans la BD"));	
			}
		} catch (IOException e) {
			e.printStackTrace();
			notifyObservers(new UIEvent(UIEvent.UIType.ShowMessage, "Erreur lors de la lecture du fichier"));
		}
	}
	private void restoreItem(String name, File file) {
		boolean success = ItemDAO.restoreItem(name, file);
		setChanged();
		
		if (success) {
			notifyObservers(new UIEvent(UIEvent.UIType.ShowMessage, "Item restaur�"));
		}
		else {
			notifyObservers(new UIEvent(UIEvent.UIType.ShowMessage, "Erreur lors de la restauration de l'item"));
		}
	}
	
	private void deleteItem(String name) {
		boolean success = ItemDAO.deleteItem(name);
		setChanged();
		
		if (success) {
			notifyObservers(new UIEvent(UIEvent.UIType.Refresh, null));
		}
		else {
			notifyObservers(new UIEvent(UIEvent.UIType.ShowMessage, "Erreur lors de la suppression de l'item"));
		}
	}
	
	public List<Item> getItemList() {
		return ItemDAO.getItemList();
	}
	
	private void login(char[] password) {
		boolean valid = UserDAO.checkPassword(password);
		
		setChanged();
		
		if (valid) {
			notifyObservers(new UIEvent(UIEvent.UIType.loginSuccessful, null));			
		}
		else {
	        notifyObservers(new UIEvent(UIEvent.UIType.ShowMessage, "Informations erronées"));
		}
	}
	
	public void exit() {
		DBConnection.releaseConnection();
		System.exit(0);
	}
}
