package ddg.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ddg.Config;
import ddg.item.entity.BaseItem;
import ddg.model.ItemEditorModel;
import ddg.model.MapEditorModel;
import ddg.utils.Utils;
import ddg.view.component.DButton;
import ddg.view.component.ListEntryCellRenderer;
import model.Cell;
import model.Map;
/**
 * This class is show map editor view
 * 
 * @author Du Zhen, Bo, Qin yi
 * @date Feb 5, 2017
 */
public class MapEditor extends JPanel implements ActionListener, ListSelectionListener {

	private ActionListener listener;
	private MapEditorModel mapsmodel;
	private Map selectedmap;
	// set the size of map. it could be changed if click the S/M/L button
	private JList list;
	public static int  MAP_SIZE = 10;
	java.util.Map<String, String> usedcell = new HashMap<>();

	boolean hasvaildpath;
	
	JPanel optionPanel;
	JPanel contentPanel;
	JPanel mapPanel;
	JPanel mapiconPanel;
	JComboBox<ImageIcon> optionsofelementoncell;
	ImageIcon floor = new ImageIcon("floor.png");
	ImageIcon chest1 = new ImageIcon("chest1.png");
	ImageIcon tree = new ImageIcon("tree.png");
	ImageIcon indoor = new ImageIcon("indoor.png");
	ImageIcon outdoor = new ImageIcon("outdoor.png");
	ImageIcon charachter = new ImageIcon("playcharacter.png");
	
	public MapEditor(ActionListener a) {
		this.listener = a;
		this.hasvaildpath = false;
		selectedmap=null;
		optionPanel = new JPanel();
		contentPanel = new JPanel();
		initData();
		initView();
		
	}

	private void initData(){
		String g = Utils.readFile(Config.MAP_FILE);
		this.mapsmodel = Utils.fromJson(g, MapEditorModel.class);
		if (this.mapsmodel == null) {
			this.mapsmodel = new MapEditorModel();
		}
	}
	
	private void initView() {
	    setLayout(new BorderLayout());
	    System.out.println(MAP_SIZE);
	    addContentPanel();
		addListView();
		list.setSelectedIndex(0);
	    addOption();
	}

	private JPanel addListView(){
		JPanel listPanel = new JPanel();
		listPanel.setPreferredSize(new Dimension(Config.BTN_WIDTH, Config.OPTION_HEIGHT/3));
		DefaultListModel l = mapsmodel.getMapListModel();
		list = new JList(l);
//		list.setCellRenderer(new ListEntryCellRenderer());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addListSelectionListener(this);
		JScrollPane mapScrollPane = new JScrollPane(list);
		mapScrollPane.setPreferredSize(new Dimension(Config.BTN_WIDTH, Config.OPTION_HEIGHT/3));
		listPanel.add(mapScrollPane);
		return listPanel;
	}
	/**
	 * This method is used for add Content panel
	 *
	 */
	private void addContentPanel(){
		mapPanel = new JPanel(){
			@Override  
	        public void paint(Graphics g) {  
	            super.paint(g);  
	            for(int i=0;i< MAP_SIZE;i++){
	                for(int j=0;j< MAP_SIZE;j++){
	                	//draw background
	                	g.drawImage(floor.getImage(), j*50, i*50, 50, 50, null);
	                	
	                    if(selectedmap != null){
	                    	if(selectedmap.getLocation()[i][j] == 'f' ){
							    g.drawImage(floor.getImage(), j*50, i*50, 50, 50, null);
							    continue;}
							if (selectedmap.getLocation()[i][j] == 't'){
								g.drawImage(tree.getImage(), j*50, i*50, 50, 50, null);
							    continue;}
							if (selectedmap.getLocation()[i][j] == 'i'){
								g.drawImage(indoor.getImage(), j*50, i*50, 50, 50, null);
								continue;}
							if (selectedmap.getLocation()[i][j] == 'c'){
								g.drawImage(chest1.getImage(), j*50, i*50, 50, 50, null);
							    continue;}
							if (selectedmap.getLocation()[i][j] == 'o'){
								g.drawImage(outdoor.getImage(), j*50, i*50, 50, 50, null);
							    continue;}
							if (selectedmap.getLocation()[i][j] == 'p'){
								g.drawImage(charachter.getImage(), j*50, i*50, 50, 50, null);
							    continue;}
	                    }
	                }
	            }
	            for(int i=0; i<MAP_SIZE; i++){
	            	g.drawLine(i*50, 0, i*50, 50*MAP_SIZE);
	            	g.drawLine(0, i*50, MAP_SIZE*50, i*50);
	            }
			}
		};
		mapPanel.setPreferredSize(new Dimension(50*MAP_SIZE, 50*MAP_SIZE));
		
		mapPanel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				int x = e.getX()/50;
				int y = e.getY()/50;
				
				ImageIcon icon = (ImageIcon)optionsofelementoncell.getSelectedItem();
				char num = icon.toString().charAt(0);
				System.out.println(x+"<>"+y);
				System.out.println(num);
				
				if(num == 'c'){
					JFrame rootframe = (JFrame) SwingUtilities.getWindowAncestor(mapPanel);
					PopUpForItem itempopup = new PopUpForItem(rootframe,"Select Item for Chect!");
					if(itempopup.getSelecteditem() != null){
						addItemInCell(itempopup.getSelecteditem(), y, x);
						selectedmap.changeLocation(y, x, 'c');
					}
				}
				else if(num =='p'){
					JFrame rootframe = (JFrame) SwingUtilities.getWindowAncestor(mapPanel);
					PopUpForFighter fighterpopup = new PopUpForFighter(rootframe,"Select Character!");
				}
				else{
					selectedmap.changeLocation(y, x, num);
				}
				
				mapPanel.repaint();
				for (int i = 0;i<MAP_SIZE; i++){
					for (int j = 0;j<MAP_SIZE; j++)
						System.out.print(selectedmap.getLocation()[i][j]);
					System.out.print("\n");
				}
				
			}
		});
		
		JScrollPane jspanel = new JScrollPane(mapPanel);
		jspanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
		jspanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jspanel.setPreferredSize(new Dimension(500, 500));
		jspanel.setBorder(Config.border);
		
		JPanel iconpanel = new JPanel();
		iconpanel.setPreferredSize(new Dimension(90, 500));
		optionsofelementoncell = new JComboBox<ImageIcon>();  
		optionsofelementoncell.addItem(floor);  
		optionsofelementoncell.addItem(tree);
		optionsofelementoncell.addItem(indoor);
		optionsofelementoncell.addItem(chest1);
		optionsofelementoncell.addItem(outdoor);
		optionsofelementoncell.addItem(charachter);
		optionsofelementoncell.setLocation(0, 0);
		iconpanel.add(optionsofelementoncell, BorderLayout.NORTH);
		iconpanel.setBorder(Config.border);
		
		contentPanel.setLayout(new FlowLayout());
		contentPanel.add(jspanel);
		contentPanel.add(iconpanel);
		add(contentPanel, BorderLayout.WEST);
	}


	public void itemPopUp() {
		// TODO Auto-generated method stub
		JFrame rootframe = (JFrame) SwingUtilities.getWindowAncestor(this);
		PopUpForItem itempopup = new PopUpForItem(rootframe,"Select Item for Chect!");
//		itempopup.setVisible(true);
		if(itempopup.getSelecteditem() != null){
			
		}
		
	}

	/**
	 * This method is used for add option panel
	 * 
	 */
	private void addOption() {
		
	    optionPanel.setPreferredSize(new Dimension(Config.OPTION_WIDTH, Config.OPTION_HEIGHT));
	    optionPanel.setBorder(Config.border);
	    JTextArea optionTitle = new JTextArea("OPTION");
	    optionTitle.setEditable(false);
	    DButton clearBtn = new DButton("CLEAR", this);
	    clearBtn.setPreferredSize(new Dimension(Config.BTN_WIDTH, Config.BTN_HEIGHT));
	    DButton validateBtn = new DButton("VALIDATE", this);
	    validateBtn.setPreferredSize(new Dimension(Config.BTN_WIDTH, Config.BTN_HEIGHT));
	    DButton saveBtn = new DButton("SAVE", this);
	    saveBtn.setPreferredSize(new Dimension(Config.BTN_WIDTH, Config.BTN_HEIGHT));
	    DButton backBtn = new DButton("BACK", this);
	    backBtn.setPreferredSize(new Dimension(Config.BTN_WIDTH, Config.BTN_HEIGHT));
	    DButton createBtn = new DButton("CREATE", this);
	    createBtn.setPreferredSize(new Dimension(Config.BTN_WIDTH, Config.BTN_HEIGHT));
	    
		JPanel listPanel = addListView();
		clearBtn.setPreferredSize(new Dimension(Config.BTN_WIDTH, Config.BTN_HEIGHT));
	    optionPanel.add(optionTitle);
	    optionPanel.add(createBtn);
	    optionPanel.add(clearBtn);
	    optionPanel.add(validateBtn);
	    optionPanel.add(saveBtn);
	    optionPanel.add(backBtn);
		optionPanel.add(listPanel);
	    add(optionPanel, BorderLayout.EAST);
	}
	
	/**
	 * 
	 * @param charoficon When draw a icon on the map, the char of icon will be recorded in to the Cells array if the icon is character or chest
	 */
	public void addItemInCell(BaseItem item, int x, int y){
		System.out.println("!!!!!!!!!!!!!!!!!!!!!");
		selectedmap.changeCellsinthemap(x, y, new Cell(item));
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("BACK")) {
			e = new ActionEvent(e.getSource(), e.getID(), "MAP-BACK");
		}
		if(e.getActionCommand().equals("VALIDATE")){
			if(checkValidation()){
				JOptionPane.showMessageDialog(null, "<html>Vaild Success!!!</html>");

			}
			else
				JOptionPane.showMessageDialog(null, "<html>The map is invalid <br> it must have a indoor, a outdoor and Feasiable Path</html>","Invalid",JOptionPane.ERROR_MESSAGE);
		}
		
		if(e.getActionCommand().equals("SAVE")){
//<<<<<<< Updated upstream;
			if(checkValidation()){
				Map.savemap(mapsmodel);
			}
			else
				JOptionPane.showMessageDialog(null, "<html>The map is invalid <br> it must have a indoor, a outdoor and Feasiable Path</html>","Invalid",JOptionPane.ERROR_MESSAGE);
		}
		if(e.getActionCommand().equals("CLEAR")){
			char maplocation[][] = selectedmap.getLocation();
			for (int i = 0;i<MAP_SIZE; i++){
				for (int j = 0;j<MAP_SIZE; j++)
					maplocation[i][j] = 'f';
			}
			mapPanel.repaint();
		}

		listener.actionPerformed(e);
	}

	public boolean checkValidation(){
		if(hasEntryDoor() && hasExitDoor()){
			return true;
		}
		return false;
	}

	public void hasValidPath(int i, int j) {
		char maplocation[][] = selectedmap.getLocation();
		if(maplocation[i][j] == 'o')
			hasvaildpath=true;
		else{
			usedcell.put(i+","+j, "i*j");
			if( j>0 && maplocation[i][j-1]!='t'){
				if(usedcell.get(i+","+ (j-1) )== null)
					hasValidPath(i, j-1);
			}
			
			if(i>0 && maplocation[i-1][j]!='t'){
				if(usedcell.get(i-1 +","+ j)== null)
					hasValidPath(i-1, j);
			}
			
			if(j<Config.MAP_SIZE-1 && maplocation[i][j+1]!='t'){
				if(usedcell.get(i +","+ (j+1))== null)
					hasValidPath(i, j+1);
			}
			
			if(i<Config.MAP_SIZE-1 && maplocation[i+1][j]!='t'){
				if(usedcell.get((i+1) +","+ j)== null)
					hasValidPath(i+1, j);
			}
		}
	}
	/**
	 * 
	 *  maplocation detail location of map.
	 * @return true if there is a indoor(Entry door) in the location of map 
	 */
	public boolean hasEntryDoor() {
		for(int i=0;i<Config.MAP_SIZE;i++){
			for(int j=0;j<Config.MAP_SIZE;j++){
				if(selectedmap.getLocation()[i][j] == 'i'){
					usedcell.clear();
					hasvaildpath = false;
					hasValidPath(i,j);
				}
			}
		}
		
		if(hasvaildpath){
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 *  maplocation detail location of map.
	 * @return true if there is a outdoor(Exit door) in the location of map 
	 */
	public boolean hasExitDoor() {
		for(int i=0;i<Config.MAP_SIZE;i++){
			for(int j=0;j<Config.MAP_SIZE;j++){
				if(selectedmap.getLocation()[i][j] == 'i'){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {
			int index = list.getSelectedIndex();
			if(index >= 0) {
				System.out.println("list select:"+index);
				Map map = mapsmodel.getMapByIndex(index);
				
				selectedmap = map;
				mapPanel.repaint();
			}
		}
	}
}
