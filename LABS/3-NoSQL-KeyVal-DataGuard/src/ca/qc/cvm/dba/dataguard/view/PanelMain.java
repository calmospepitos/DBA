package ca.qc.cvm.dba.dataguard.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import ca.qc.cvm.dba.dataguard.app.Facade;
import ca.qc.cvm.dba.dataguard.entity.Item;
import ca.qc.cvm.dba.dataguard.event.AddItemEvent;
import ca.qc.cvm.dba.dataguard.event.DeleteItemEvent;
import ca.qc.cvm.dba.dataguard.event.RestoreItemEvent;
import ca.qc.cvm.dba.dataguard.view.util.BackgroundPanel;

public class PanelMain extends CommonPanel {
	private static final long serialVersionUID = 1L;

	private JButton addBtn;
	private JButton viewBtn;
	private JButton deleteBtn;
	private JList itemList;

	public PanelMain(int width, int height) throws Exception {
		super(width, height);
		jbInit();
	}
	
	public void jbInit() throws Exception {
		this.setPreferredSize(new Dimension(width, height));
		this.setBackground(Color.WHITE);
		
		this.setLayout(null);

		itemList = (JList)this.addField(new JList(), 330, 10, 250, 200);
		itemList.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		itemList.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (!itemList.isSelectionEmpty()) {
					viewBtn.setVisible(true);
					deleteBtn.setVisible(true);
				}
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
		
		addBtn = this.addButton("Ajouter un fichier", 20, 20, 200, 30, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
		    	JFileChooser fileChooser = new JFileChooser();
	            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	            int resultat = fileChooser.showOpenDialog(PanelMain.this);

	            if (resultat != JFileChooser.CANCEL_OPTION) {
	                File fileTmp = fileChooser.getSelectedFile();
					Facade.getInstance().processEvent(new AddItemEvent(fileTmp));
	            }
			}
		});

		viewBtn = this.addButton("Prendre", 225, 160, 100, 20, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fileChooser = new JFileChooser();
	            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	            int resultat = fileChooser.showSaveDialog(PanelMain.this);

	            if (resultat != JFileChooser.CANCEL_OPTION) {
	                File fileTmp = fileChooser.getSelectedFile();
					Facade.getInstance().processEvent(new RestoreItemEvent(((Item)itemList.getSelectedValue()).getName(), fileTmp));
	            }
			}
		});

		deleteBtn = this.addButton("Supprimer", 225, 190, 100, 20, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(PanelMain.this, "Supprimer cet élément?");
				
				if (result == JOptionPane.OK_OPTION) {
					Facade.getInstance().processEvent(new DeleteItemEvent(((Item)itemList.getSelectedValue()).getName()));
				}
			}
		});
		
		
		this.add(new BackgroundPanel("assets/images/background.jpg", width, height), new Dimension(0, 0));
	}
	
	@Override
	public void resetUI() {
		viewBtn.setVisible(false);
		deleteBtn.setVisible(false);
		itemList.setListData(Facade.getInstance().getItemList().toArray());
	}

}
