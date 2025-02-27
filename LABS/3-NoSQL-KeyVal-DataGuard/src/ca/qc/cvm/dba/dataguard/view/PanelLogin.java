package ca.qc.cvm.dba.dataguard.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPasswordField;

import ca.qc.cvm.dba.correctionserver.lib.CorrectionDialog;
import ca.qc.cvm.dba.dataguard.app.Facade;
import ca.qc.cvm.dba.dataguard.event.GeneralEvent;
import ca.qc.cvm.dba.dataguard.event.GeneralEvent.GeneralType;
import ca.qc.cvm.dba.dataguard.event.LoginEvent;
import ca.qc.cvm.dba.dataguard.view.util.BackgroundPanel;

public class PanelLogin extends CommonPanel {
	private static final long serialVersionUID = 1L;
	private JPasswordField pwdInput;

	public PanelLogin(int width, int height) throws Exception {
		super(width, height);
		jbInit();
	}
	
	public void jbInit() throws Exception {
		this.setPreferredSize(new Dimension(width, height));
		this.setBackground(Color.WHITE);
		
		this.setLayout(null);

		JLabel title = super.addLabel("DataGuard", 0, 30, width, 50);
		title.setForeground(Color.WHITE);
		title.setFont(new Font("Arial", Font.BOLD, 40));
		title.setHorizontalAlignment(JLabel.CENTER);
						
		pwdInput = (JPasswordField)super.addField(new JPasswordField(), 150, 100, 300, LINE_HEIGHT);
		
		super.addButton("Valider", 250, 150, 100, LINE_HEIGHT, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Facade.getInstance().processEvent(new LoginEvent(pwdInput.getPassword()));
			}			
		});		

		super.addButton("Correction", 475, 10, 100, LINE_HEIGHT, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String[] data = CorrectionDialog.getData(PanelLogin.this);
				
				if (data != null) {
					Facade.getInstance().processEvent(new GeneralEvent(GeneralType.Correct, data[0], data[1], data[2]));
				}
			}			
		});

		super.addLabel("Par : Le prof de DBA", 10, 510, 200, LINE_HEIGHT);
		super.addLabel("Version : 0.1", 10, 510 + LINE_HEIGHT, 100, LINE_HEIGHT);
		
		this.add(new BackgroundPanel("assets/images/background.jpg", width, height), new Dimension(0, 0));
		
	}
	
	@Override
	public void resetUI() {
		pwdInput.setText("");
	}

}
