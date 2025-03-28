package ca.qc.cvm.dba.magix.view;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import ca.qc.cvm.dba.magix.app.Facade;
import ca.qc.cvm.dba.magix.event.ExitEvent;
import ca.qc.cvm.dba.magix.event.UIEvent;

public class FrameMain extends JFrame implements Observer {
	private static final long serialVersionUID = 1L;
	private State currentScene;
	
	public enum State {Splash, About, Choose, Game, Info}
	
	private HashMap<State, Scene> scenes;
	
	public FrameMain() throws Exception {
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		Facade.getInstance().addObserverClass(this);
        
		this.setTitle("Magix");
		this.setIconImage(new ImageIcon("assets/images/icon.png").getImage());
		this.setSize(new Dimension(950, 650));
        this.setResizable(false);

		Facade.getInstance().testConnection();
		 		 
		addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
            	Facade.getInstance().processEvent(new ExitEvent());
            }
		});

		Toolkit g = this.getToolkit();
		int x = (g.getScreenSize().width / 2) - 950/2;
		int y = (g.getScreenSize().height / 2) - 650/2;
		this.setLocation(x, y);

		scenes = new HashMap<State, Scene>();		
		scenes.put(State.Splash, new SceneSplash());
		scenes.put(State.Info, new SceneInfo());
		scenes.put(State.About, new SceneAbout());
		scenes.put(State.Choose, new SceneChoose());
		scenes.put(State.Game, new SceneGame());		
		
		this.setVisible(true);

		Image image = Toolkit.getDefaultToolkit().getImage("assets/images/cursor.png");
		Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(image , new Point(0, 0), "main-cursor");
		
		this.setCursor(c);
		
		this.setScene(State.Splash);
	}
		
	/**
	 * Permet d'afficher un autre panel. Vous ne devriez pas avoir à
	 * appeler cette méthode.
	 * 
	 * @param state
	 */
	public void setScene(State state) {
		if (currentScene != state) {
			for (Scene p : scenes.values()) {
				this.getContentPane().remove(p);
			}

			scenes.get(state).resetView();
			
			this.getContentPane().add(scenes.get(state));
			this.revalidate();
			this.repaint();
		}

		currentScene = state;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		UIEvent event = ((UIEvent)arg);
		
		if (event.getUIType() == UIEvent.UIType.GoTo) {
			this.setScene(State.valueOf(event.getData().toString()));
		}
		else if (event.getUIType() == UIEvent.UIType.Refresh) {
			scenes.get(currentScene).resetView();
		}
		else if (event.getUIType() == UIEvent.UIType.ShowMessage) {
			JOptionPane.showMessageDialog(null, event.getData() + "");
		}
	}
}