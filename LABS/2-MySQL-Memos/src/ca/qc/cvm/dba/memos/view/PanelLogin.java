package ca.qc.cvm.dba.memos.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import ca.qc.cvm.dba.correctionserver.lib.CorrectionDialog;
import ca.qc.cvm.dba.memos.app.Facade;
import ca.qc.cvm.dba.memos.event.GeneralEvent;
import ca.qc.cvm.dba.memos.event.GeneralEvent.GeneralType;
import ca.qc.cvm.dba.memos.event.GoToEvent;
import ca.qc.cvm.dba.memos.event.LoginEvent;
import ca.qc.cvm.dba.memos.view.util.BackgroundPanel;

public class PanelLogin extends CommonPanel {
	private static final long serialVersionUID = 1L;
	private JTextField loginInput;
	private JPasswordField pwdInput;

	public PanelLogin(int width, int height) throws Exception {
		super(width, height);
		jbInit();
	}
	
	public void jbInit() throws Exception {
		this.setPreferredSize(new Dimension(width, height));
		this.setBackground(Color.WHITE);
		
		this.setLayout(null);
		
		JLabel title = super.addLabel("Mes Mémos", 0, 100, 700, 50);
		title.setForeground(new Color(123, 109, 95));
		title.setFont(new Font("Arial", Font.BOLD, 40));
		title.setHorizontalAlignment(JLabel.CENTER);
		
		this.addButton("Correction", 580, 10, 100, 20, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] data = CorrectionDialog.getData(PanelLogin.this);
				
				if (data != null) {
					Facade.getInstance().processEvent(new GeneralEvent(GeneralType.Correct, data[0], data[1], data[2]));
				}
			}
		});
		
		this.addButton("S'enregistrer", 580, 530, 100, 20, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Facade.getInstance().processEvent(new GoToEvent(GoToEvent.Destination.Register));
			}
		});
		
		super.addLabel("Nom d'usager : ", 10, 280, 100, LINE_HEIGHT).setForeground(Color.WHITE);
		loginInput = (JTextField)super.addField(new JTextField(), 110, 280, 200, LINE_HEIGHT);
		super.addLabel("Mot de passe : ", 10, 310, 100, LINE_HEIGHT).setForeground(Color.WHITE);
		pwdInput = (JPasswordField)super.addField(new JPasswordField(), 110, 310, 200, LINE_HEIGHT);
		
		super.addButton("Valider", 210, 360, 100, LINE_HEIGHT, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Facade.getInstance().processEvent(new LoginEvent(loginInput.getText(), pwdInput.getPassword()));
			}			
		});

		super.addLabel("Par : Vinicius et Alexia", 10, 510, 200, LINE_HEIGHT);
		super.addLabel("Version : 0.1", 10, 510 + LINE_HEIGHT, 100, LINE_HEIGHT);
		
		this.add(new BackgroundPanel("assets/images/background-1.png", width, height), new Dimension(0, 0));
		
	}
	
	@Override
	public void resetUI() {
		loginInput.setText("");
		pwdInput.setText("");
	}

}
