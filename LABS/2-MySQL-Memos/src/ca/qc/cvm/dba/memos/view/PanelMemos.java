package ca.qc.cvm.dba.memos.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import ca.qc.cvm.dba.memos.app.Facade;
import ca.qc.cvm.dba.memos.entity.Category;
import ca.qc.cvm.dba.memos.entity.Memo;
import ca.qc.cvm.dba.memos.event.AddCategoryEvent;
import ca.qc.cvm.dba.memos.event.AddMemoEvent;
import ca.qc.cvm.dba.memos.event.DeleteCategoryEvent;
import ca.qc.cvm.dba.memos.event.DeleteMemoEvent;
import ca.qc.cvm.dba.memos.event.GoToEvent;
import ca.qc.cvm.dba.memos.event.SearchMemosEvent;
import ca.qc.cvm.dba.memos.view.util.BackgroundPanel;

public class PanelMemos extends CommonPanel {
	private static final long serialVersionUID = 1L;
	
	private JTextField memoInput;
	private JComboBox<String> categoriesInput;
	private JList memoList;
	private JButton deleteBtn;
	private JComboBox<String> searchCategoryInput;
	private JTextField searchInput;
	
	private List<Category> categories;

	public PanelMemos(int width, int height) throws Exception {
		super(width, height);
		jbInit();
	}
	
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
		
		this.addLabel("Nouveau mémo : ", 30, 70, 90, LINE_HEIGHT).setForeground(Color.WHITE);
		categoriesInput = (JComboBox<String>)this.addField(new JComboBox<String>(), 130, 70, 100, LINE_HEIGHT);
		memoInput = (JTextField)this.addField(new JTextField(), 240, 70, 250, LINE_HEIGHT);
		
		this.addButton("Ajouter", 500, 70, 100, LINE_HEIGHT, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Facade.getInstance().processEvent(new AddMemoEvent(categories.get(categoriesInput.getSelectedIndex()).getId(), memoInput.getText()));
			}
		});
		/*
		searchCategoryInput = (JComboBox<String>)this.addField(new JComboBox<String>(), 10, 250 - 10 - LINE_HEIGHT, 100, LINE_HEIGHT);
		searchInput = (JTextField)this.addField(new JTextField(), 120, 250 - 10 - LINE_HEIGHT, 200, LINE_HEIGHT);
		this.addButton("Chercher", 330, 250 - 10 - LINE_HEIGHT, 100, LINE_HEIGHT, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Integer categoryId = null;
				
				if (searchCategoryInput.getSelectedIndex() > 0) {
					categoryId = categories.get(searchCategoryInput.getSelectedIndex() -1).getId();
				}
				
				Facade.getInstance().processEvent(new SearchMemosEvent(categoryId, searchInput.getText()));
			}
		});
		*/
		
		memoList = (JList)this.addField(new JList(), 10, 250, width - 25, 200);
		memoList.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		memoList.addMouseListener(new MouseListener() {

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
		
		deleteBtn = this.addButton("Supprimer", width - 115, 460, 100, 20, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(PanelMemos.this, "Supprimer ce mémo?");
				
				if (result == JOptionPane.OK_OPTION) {
					Facade.getInstance().processEvent(new DeleteMemoEvent(((Memo)memoList.getSelectedValue()).getId()));
				}
			}
		});
		
		this.add(new BackgroundPanel("assets/images/background-2.png", width, height), new Dimension(0, 0));
	}
	
	@Override
	public void resetUI() {
		memoInput.setText("");
		categoriesInput.removeAllItems();
		//searchCategoryInput.removeAllItems();
		
		categories = Facade.getInstance().getCategoryList();
		
		//searchCategoryInput.addItem("--");
		
		for (Category category : categories) {
			categoriesInput.addItem(category.getName());
			//searchCategoryInput.addItem(category.getName());
		}
		
		//searchInput.setText("");
		
		memoList.setListData(Facade.getInstance().getMemoList().toArray());
		deleteBtn.setVisible(false);
	}
	
	public void setMemos(List<Memo> memos) {
		memoList.setListData(memos.toArray());
		deleteBtn.setVisible(false);
	}

}
