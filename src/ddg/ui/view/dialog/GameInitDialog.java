package ddg.ui.view.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import ddg.Config;
import ddg.model.CampaignEditorModel;
import ddg.model.Fighter;
import ddg.model.FighterModel;
import ddg.model.GameModel;
import ddg.model.entity.BaseCampaign;
import ddg.ui.view.component.DButton;
import ddg.ui.view.component.DComboBox;
import ddg.ui.view.component.DComboBox.DItemListener;
import ddg.utils.Utils;
/**
 * 
 * A dialog to choose Character and Campaign
 * 
 * @author Zhen Du
 * @date Mar 12, 2017
 */
public class GameInitDialog extends JDialog implements ActionListener, DItemListener {

	private GameModel gameModel;
	private DButton okBtn;
	private DComboBox<String> characterComboBox;
	private DComboBox<String> campaignComboBox;
	private FighterModel fighterModel;
	private CampaignEditorModel campaignModel;
	private Fighter selectFighter;
	private BaseCampaign selectCampaign;
	private JCheckBox checkboxComputer;
	
	/**
	 * 
	 * Constructors for GameInitDialog
	 * 
	 * @param owner
	 * @param title
	 */
	public GameInitDialog(Frame owner, String title) {
		super(owner, title, true);
		initView();
		initData();
	}

	/**
	 * 
	 * This method showSelectView
	 *
	 */
	public void showSelectView() {
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * 
	 * This method getGameModel
	 * 
	 * @return
	 */
	public GameModel getGameModel() {
		return this.gameModel;
	}
	
	/**
	 * 
	 * This method initView
	 *
	 */
	private void initView() {
		JPanel panel = new JPanel();
		JPanel selectPanel = new JPanel();
		
		checkboxComputer = new JCheckBox("Computer?");
		
		characterComboBox = new DComboBox<String>("Character");
		characterComboBox.addDItemListener(this);
		
		campaignComboBox = new DComboBox<String>("Campaign");
		campaignComboBox.addDItemListener(this);
		selectPanel.add(characterComboBox);
		selectPanel.add(campaignComboBox);
		selectPanel.add(checkboxComputer);
		panel.add(selectPanel);
		
		okBtn = new DButton("OK", this);
		okBtn.setEnabled(false);
		okBtn.setPreferredSize(new Dimension(Config.BTN_WIDTH, Config.BTN_HEIGHT));
		DButton cancelBtn = new DButton("CANCEL", this);
		cancelBtn.setPreferredSize(new Dimension(Config.BTN_WIDTH, Config.BTN_HEIGHT));
		panel.add(okBtn);
		panel.add(cancelBtn);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(selectPanel, BorderLayout.NORTH);
		getContentPane().add(panel, BorderLayout.SOUTH);
	}
	
	/**
	 * 
	 * This method initData
	 *
	 */
	private void initData() {
//		fighterModel = new FighterModel();
//		String g = Utils.readFile(Config.CHARACTER_FILE);
//		fighterModel = Utils.fromJson(g, FighterModel.class);
		fighterModel = Utils.readObject(Config.CHARACTER_FILE, FighterModel.class);
		if (fighterModel != null) {
			for(String i : this.fighterModel.getFighterList()) {
				characterComboBox.addItem(i);
			}
		}
		
		String g = Utils.readFile(Config.CAMPAIGN_FILE);
		this.campaignModel = Utils.fromJson(g, CampaignEditorModel.class);
		if (this.campaignModel != null) {
			for(String i : this.campaignModel.getCampaignList()) {
				campaignComboBox.addItem(i);
			}
		}
		if(characterComboBox.getItemCount() > 0 && campaignComboBox.getItemCount() > 0) {
			okBtn.setEnabled(true);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("OK")) {
			System.out.println("OK");
			if(selectFighter!=null&&selectCampaign!=null) {
				this.gameModel = mapLoading(selectFighter, selectCampaign);
				this.gameModel.setComputer(checkboxComputer.isSelected());
			}
		} else if (e.getActionCommand().equals("CANCEL")) {
		}
		dispose();
	}
	/**
	 * 
	  * This method load fighter and baseCampaign in GameModle
	  * @param fighter
	  * @param baseCampaign
	  * @return GameModel
	 */
	public GameModel mapLoading(Fighter fighter, BaseCampaign baseCampaign){
		if(fighter != null && baseCampaign != null){
			return new GameModel(fighter, baseCampaign);
		}else 
			return null;
	}

	@Override
	public void itemChanged(ItemEvent e, String name) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			System.out.println("ComboBox " + name + " choose " + e.getItem());
			if ("Character".equals(name)) {
				String n = (String) characterComboBox.getSelectedItem();
				selectFighter = fighterModel.getFighterByName(n);
			} else if ("Campaign".equals(name)) {
				int index = campaignComboBox.getSelectedIndex();
				selectCampaign = campaignModel.getItemByIndex(index);
			}
		}
	}
}
