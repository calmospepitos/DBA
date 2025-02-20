package ca.qc.cvm.dba.memos.view;

import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import ca.qc.cvm.dba.memos.app.Facade;
import ca.qc.cvm.dba.memos.entity.Memo;
import ca.qc.cvm.dba.memos.event.UIEvent;

public class FrameMain extends JFrame implements Observer {
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 700;
	private static final int HEIGHT = 600;
	
	public enum State {Login, Menu, Categories, Memos, Register}
	private State currentState;
	
	private HashMap<State, CommonPanel> panels;
	
	public FrameMain() throws Exception {
		Facade.getInstance().addObserverClass(this);
		jbInit();
	}
	
	private void jbInit() throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        
        this.setIconImage(new ImageIcon("assets/images/logo.png").getImage());
        this.setResizable(false);
		this.setSize(WIDTH, HEIGHT);
		
		this.setTitle("Mes mémos");
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {}

			@Override
			public void windowClosing(WindowEvent e) {
				Facade.getInstance().exit();
			}

			@Override
			public void windowClosed(WindowEvent e) {}

			@Override
			public void windowIconified(WindowEvent e) {}

			@Override
			public void windowDeiconified(WindowEvent e) {}

			@Override
			public void windowActivated(WindowEvent e) {}

			@Override
			public void windowDeactivated(WindowEvent e) {	}
        	
        });

		Toolkit g = this.getToolkit();
		int x = (g.getScreenSize().width / 2) - WIDTH/2;
		int y = (g.getScreenSize().height / 2) - HEIGHT/2;
		this.setLocation(x, y);
		
		panels = new HashMap<State, CommonPanel>();		
		panels.put(State.Login, new PanelLogin(WIDTH, HEIGHT));
		panels.put(State.Register, new PanelRegister(WIDTH, HEIGHT));
		panels.put(State.Menu, new PanelMenu(WIDTH, HEIGHT));
		panels.put(State.Categories, new PanelCategories(WIDTH, HEIGHT));
		panels.put(State.Memos, new PanelMemos(WIDTH, HEIGHT));
		
		this.setState(State.Login);
	}
		
	public void setState(State state) {
		if (currentState != state) {
			for (CommonPanel p : panels.values()) {
				this.getContentPane().remove(p);
			}

			panels.get(state).resetUI();
			
			this.getContentPane().add(panels.get(state));
			this.revalidate();
			this.repaint();
		}

		currentState = state;
	}

	@Override
	public void update(Observable o, Object arg) {
		UIEvent event = ((UIEvent)arg);
		
		if (event.getUIType() == UIEvent.UIType.ShowMessage) {
			JOptionPane.showMessageDialog(this, event.getData().toString());
		}
		else if (event.getUIType() == UIEvent.UIType.loginSuccessful) {
			this.setState(State.Menu);
		}
		else if (event.getUIType() == UIEvent.UIType.GoTo) {
			this.setState(State.valueOf(event.getData().toString()));
		}
		else if (event.getUIType() == UIEvent.UIType.Refresh) {
			panels.get(currentState).resetUI();
		}
		else if (event.getUIType() == UIEvent.UIType.MemosUpdated) {
			((PanelMemos)panels.get(currentState)).setMemos((List<Memo>)event.getData());
		}
	}
}
