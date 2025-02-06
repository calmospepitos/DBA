package ca.qc.cvm.dba.jumper.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import ca.qc.cvm.dba.correctionserver.lib.CorrectionDialog;
import ca.qc.cvm.dba.jumper.app.Facade;
import ca.qc.cvm.dba.jumper.event.CorrectionEvent;
import ca.qc.cvm.dba.jumper.event.GoToEvent;

public class PanelMainMenu extends CommonPanel {
	private static final long serialVersionUID = 1L;

	public PanelMainMenu(int width, int height) throws Exception {
		super(width, height, false, "assets/images/space.jpg");
	}
	
	@Override
	public void jbInit() throws Exception {
		JLabel logo = this.addLabel("", this.width/2 - 150, 100, 300, 200);
		logo.setIcon(new ImageIcon("assets/images/jumper-logo.png"));
		
		this.addButton("Démarrer", this.width/2 - 75, this.height/2 + 50, 150, 40, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Facade.getInstance().processEvent(new GoToEvent(FrameMain.Views.Game));
			}
		});
		
		this.addButton("Statistiques", this.width/2 - 75, this.height/2 + 100, 150, 40, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Facade.getInstance().processEvent(new GoToEvent(FrameMain.Views.Stats));
			}
		});
		
		this.addButton("Quitter", this.width/2 - 75, this.height/2 + 150, 150, 40, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(PanelMainMenu.this, "Voulez-vous vraiment quitter?");
				
				if (result == JOptionPane.OK_OPTION) {
					Facade.getInstance().exit();
				}
			}
		});
		
		this.addButton("Correction", 10, 10, 150, 20, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String[] data = CorrectionDialog.getData(null);
				
				if (data != null) {
					Facade.getInstance().processEvent(new CorrectionEvent(data[0], data[1], data[2]));
				}
			}
		});
	}
	
	/**
	 * Cette méthode est appelée automatiquement à chaque fois qu'un panel est affiché (lorsqu'on arrive sur la page)
	 */
	@Override
	public void resetUI() {
	}

}
