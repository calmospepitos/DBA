package ca.qc.cvm.dba.memos.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import ca.qc.cvm.dba.memos.app.Facade;
import ca.qc.cvm.dba.memos.entity.Category;
import ca.qc.cvm.dba.memos.event.AddCategoryEvent;
import ca.qc.cvm.dba.memos.event.DeleteCategoryEvent;
import ca.qc.cvm.dba.memos.event.GoToEvent;
import ca.qc.cvm.dba.memos.view.util.BackgroundPanel;

public class PanelCategories extends CommonPanel {
	private static final long serialVersionUID = 1L;

	private JTextField categoryInput;
	private JButton deleteBtn;
	private JList categoryList;

	public PanelCategories(int width, int height) throws Exception {
		super(width, height);
		jbInit();
	}
	
	@SuppressWarnings("unchecked")
	public void jbInit() throws Exception {
		this.setPreferredSize(new Dimension(width, height));
		this.setBackground(Color.WHITE);
		
		this.setLayout(null);
		
		this.addButton("Retour", 10, 10, 100, 20, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Facade.getInstance().processEvent(new GoToEvent(GoToEvent.Destination.Menu));
			}
		});
		
		this.addLabel("Nouvelle catégorie : ", 140, 70, 100, LINE_HEIGHT).setForeground(Color.WHITE);
		categoryInput = (JTextField)this.addField(new JTextField(), 250, 70, 150, LINE_HEIGHT);
		
		this.addButton("Ajouter", 410, 70, 100, LINE_HEIGHT, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Facade.getInstance().processEvent(new AddCategoryEvent(categoryInput.getText()));
			}
		});
		
		categoryList = (JList)this.addField(new JList(), 200, 250, 300, 200);
		categoryList.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		categoryList.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				deleteBtn.setVisible(true);
			}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
			
		});
		
		deleteBtn = this.addButton("Supprimer", 400, 460, 100, 20, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(PanelCategories.this, "Supprimer cette catégorie et ses mémos?");
				
				if (result == JOptionPane.OK_OPTION) {
					Facade.getInstance().processEvent(new DeleteCategoryEvent(((Category)categoryList.getSelectedValue()).getId()));
				}
			}
		});
		
		this.add(new BackgroundPanel("assets/images/background-2.png", width, height), new Dimension(0, 0));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void resetUI() {
		categoryInput.setText("");
		categoryList.setListData(Facade.getInstance().getCategoryList().toArray());
		deleteBtn.setVisible(false);
	}

}
