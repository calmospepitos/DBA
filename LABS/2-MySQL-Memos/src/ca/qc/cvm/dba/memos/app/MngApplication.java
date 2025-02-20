package ca.qc.cvm.dba.memos.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import ca.qc.cvm.dba.memos.dao.CategoryDAO;
import ca.qc.cvm.dba.memos.dao.DBConnection;
import ca.qc.cvm.dba.memos.dao.MemoDAO;
import ca.qc.cvm.dba.memos.dao.UserDAO;
import ca.qc.cvm.dba.memos.entity.Category;
import ca.qc.cvm.dba.memos.entity.Memo;
import ca.qc.cvm.dba.memos.entity.User;
import ca.qc.cvm.dba.memos.event.AddCategoryEvent;
import ca.qc.cvm.dba.memos.event.AddMemoEvent;
import ca.qc.cvm.dba.memos.event.CommonEvent;
import ca.qc.cvm.dba.memos.event.DeleteCategoryEvent;
import ca.qc.cvm.dba.memos.event.DeleteMemoEvent;
import ca.qc.cvm.dba.memos.event.GeneralEvent;
import ca.qc.cvm.dba.memos.event.GeneralEvent.GeneralType;
import ca.qc.cvm.dba.memos.event.RegisterEvent;
import ca.qc.cvm.dba.memos.event.SearchMemosEvent;
import ca.qc.cvm.dba.memos.event.GoToEvent;
import ca.qc.cvm.dba.memos.event.LoginEvent;
import ca.qc.cvm.dba.memos.event.UIEvent;

public class MngApplication extends Observable implements Runnable {
    private List<CommonEvent> eventQueue;
    private User connectedUser;
    
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
			LoginEvent loginEvent = (LoginEvent)event;
			login(loginEvent.getUsername(), loginEvent.getPassword());
		}
		else if (event.getType() == CommonEvent.Type.Register) {
			RegisterEvent registerEvent = (RegisterEvent)event;
			register(registerEvent.getUsername(), registerEvent.getPassword());
		}
		else if (event.getType() == CommonEvent.Type.AddCategory) {
			addCategory(((AddCategoryEvent)event).getName());
		}
		else if (event.getType() == CommonEvent.Type.DeleteCategory) {
			deleteCategory(((DeleteCategoryEvent)event).getId());
		}
		else if (event.getType() == CommonEvent.Type.AddMemo) {
			addMemo(((AddMemoEvent)event).getCategoryId(), ((AddMemoEvent)event).getText());
		}
		else if (event.getType() == CommonEvent.Type.DeleteMemo) {
			deleteMemo(((DeleteMemoEvent)event).getId());
		}
		else if (event.getType() == CommonEvent.Type.SearchMemos) {
			searchMemos(((SearchMemosEvent)event).getCategoryId(), ((SearchMemosEvent)event).getText());
		}
		else if (event.getType() == CommonEvent.Type.GoTo) {
			setChanged();
			notifyObservers(new UIEvent(UIEvent.UIType.GoTo, ((GoToEvent)event).getDestination().toString()));
		}
		else if (event.getType() == CommonEvent.Type.General) {
			GeneralEvent evt = (GeneralEvent)event;
			
			if (evt.getGeneralType() == GeneralType.Correct) {
				CorClient client = new CorClient();
				client.start(evt.getText(), evt.getText2(), evt.getText3());
			}
		}
	}
	
	private void deleteCategory(int id) {
		boolean success = CategoryDAO.deleteCategory(id);
		
		setChanged();
		
		if (success) {			
			notifyObservers(new UIEvent(UIEvent.UIType.Refresh, null));
		}
		else {
			notifyObservers(new UIEvent(UIEvent.UIType.ShowMessage, "Erreur de suppression de catégorie"));
		}
	}
	
	private void addCategory(String name) {
		boolean success = CategoryDAO.addCategory(connectedUser.getId(), name);
		
		setChanged();
		
		if (success) {			
			notifyObservers(new UIEvent(UIEvent.UIType.Refresh, null));
		}
		else {
			notifyObservers(new UIEvent(UIEvent.UIType.ShowMessage, "Erreur : Catégorie déjà existante ou vide"));
		}
	}
	
	private void addMemo(int categoryId, String text) {
		boolean success = MemoDAO.addMemo(categoryId, text);
		
		setChanged();
		
		if (success) {			
			notifyObservers(new UIEvent(UIEvent.UIType.Refresh, null));
		}
		else {
			notifyObservers(new UIEvent(UIEvent.UIType.ShowMessage, "Erreur : Impossible d'ajouter le mémo"));
		}
	}
	
	private void deleteMemo(int id) {
		boolean success = MemoDAO.deleteMemo(id);
		
		setChanged();
		
		if (success) {			
			notifyObservers(new UIEvent(UIEvent.UIType.Refresh, null));
		}
		else {
			notifyObservers(new UIEvent(UIEvent.UIType.ShowMessage, "Erreur de suppression du mémo"));
		}
	}
	
	private void searchMemos(Integer categoryId, String text) {
		List<Memo> memos = MemoDAO.searchMemos(connectedUser.getId(), categoryId, text);
		
		setChanged();
		notifyObservers(new UIEvent(UIEvent.UIType.MemosUpdated, memos));
	}
	
	private void login(String username, char[] password) {
		User user = UserDAO.login(username, password);
		
		setChanged();
		
		if (user != null) {
			connectedUser = user;
			notifyObservers(new UIEvent(UIEvent.UIType.loginSuccessful, null));			
		}
		else {
	        notifyObservers(new UIEvent(UIEvent.UIType.ShowMessage, "Informations erronées"));
		}
	}

	private void register(String username, char[] password) {
		boolean success = UserDAO.register(username, password);
		
		setChanged();
		
		if (success) {
			notifyObservers(new UIEvent(UIEvent.UIType.ShowMessage, "Bravo! Vous êtes maintenant enregistré dans le système. \nVous pouvez maintenant vous connecter"));
			setChanged();
			notifyObservers(new UIEvent(UIEvent.UIType.GoTo, "Login"));			
		}
		else {
	        notifyObservers(new UIEvent(UIEvent.UIType.ShowMessage, "Erreur d'enregistrement. Avez-vous utilisé le nom d'un membre existant?"));
		}
	}
	
	public List<Category> getCategoryList() {
		return CategoryDAO.getCategoryList(connectedUser.getId());
	}
	
	public List<Memo> getMemoList() {
		return MemoDAO.getMemoList(connectedUser.getId());
	}
	
	public void testConnection() {
		boolean success = DBConnection.connectionCredentialsValid();
		
		if (!success) {
			setChanged();
			notifyObservers(new UIEvent(UIEvent.UIType.ShowMessage, "Informations de connexion invalides (impossible de se connecter à la BD"));
			exit();
		}
	}
	
	public void exit() {
		DBConnection.releaseConnection();
		System.exit(0);
	}
}
