package ca.qc.cvm.dba.memos.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import ca.qc.cvm.dba.memos.app.Facade;
import ca.qc.cvm.dba.memos.event.GoToEvent;
import ca.qc.cvm.dba.memos.view.util.BackgroundPanel;

public class PanelMenu extends CommonPanel {
	private static final long serialVersionUID = 1L;

	public PanelMenu(int width, int height) throws Exception {
		super(width, height);
		jbInit();
	}
	
	public void jbInit() throws Exception {
		this.setPreferredSize(new Dimension(width, height));
		this.setBackground(Color.WHITE);
		
		this.setLayout(null);
		
		this.addButton("Les catégories", 110, 310, 150, 40, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Facade.getInstance().processEvent(new GoToEvent(GoToEvent.Destination.Categories));
			}
		});
		
		this.addButton("Les mémos", 310, 310, 150, 40, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (Facade.getInstance().getCategoryList().size() == 0) {
					JOptionPane.showMessageDialog(PanelMenu.this, "Impossible! Vous n'avez pas encore défini de catégories");
				}
				else {
					Facade.getInstance().processEvent(new GoToEvent(GoToEvent.Destination.Memos));
				}
			}
		});
		
		this.add(new BackgroundPanel("assets/images/background-1.png", width, height), new Dimension(0, 0));
	}
	
	@Override
	public void resetUI() {
	}

}
