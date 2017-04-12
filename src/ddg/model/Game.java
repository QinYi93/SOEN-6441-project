package ddg.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ddg.Config;
import ddg.IOwner;
import ddg.model.entity.BaseCampaign;
import ddg.strategy.HumanStrategy;
import ddg.strategy.IStrategy.TurnCallback;

public class Game implements IOwner, java.io.Serializable{
	private static final long serialVersionUID = -1424213104639818704L;
	
	private BaseCampaign campaign;
	private Map playingmap;
	private Fighter fighter;
	private int xofplayer;
	private int yofplayer;
	
	public Game(Fighter player, BaseCampaign campaign){
		this.fighter = player.clone();
		this.campaign = campaign;
		if(playingmap==null){
			initData();
		}
		this.fighter.setStrategy(new HumanStrategy());
	}
	
	private void initData(){
		if(campaign.getMaps().size()==0){
			playingmap=null;
			return;
		}
		
		try{
			//read maps files import maps
			FileInputStream fileIn = new FileInputStream(Config.MAP_FILE);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			MapEditorModel mapsmodel = (MapEditorModel) in.readObject();
			in.close();
			fileIn.close();
			
			if (mapsmodel == null) {
				mapsmodel = new MapEditorModel();
				mapsmodel.add(new Map());
				System.out.println("map files was wrong!!!");
				System.exit(0);
			}
			for(Map map : mapsmodel.getMaps()){
				if(map.getName().equals(campaign.getMaps().get(0))){
					this.playingmap = map;
					initMapData();
					break;
				}
			}
			
		}catch(IOException i){
			i.printStackTrace();
		}catch(ClassNotFoundException c){
			c.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void initMapData() {
		// TODO Auto-generated method stub
//		this.playingmap.adaptedLevel(fighter.getLevel());
		this.playingmap.setOwner(this);
		
		for(int i=0;i<playingmap.getRow();i++){
			for(int j=0;j<playingmap.getColumn();j++){
				if(playingmap.getLocation()[i][j] == 'i'){
					if(i>0 && playingmap.getLocation()[i-1][j]=='f'){
						 xofplayer = i-1;
						 yofplayer = j;
					}
					else if(i<playingmap.getRow()-1 && playingmap.getLocation()[i+1][j]=='f'){
						xofplayer = i+1;
						yofplayer = j;
					}
					else if(j>0 && playingmap.getLocation()[i][j-1]=='f'){
						xofplayer = i;
						yofplayer = j-1;
					}
					else if(j<playingmap.getColumn()-1 && playingmap.getLocation()[i][j+1]=='f'){
						xofplayer = i;
						yofplayer = j+1;
					}
				}
			}
		}
		
	}

	public int getXofplayer() {
		return xofplayer;
	}

	public void setXofplayer(int xofplayer) {
		this.xofplayer = xofplayer;
	}

	public int getYofplayer() {
		return yofplayer;
	}

	public void setYofplayer(int yofplayer) {
		this.yofplayer = yofplayer;
	}

	public BaseCampaign getCampaign() {
		return campaign;
	}

	public Map getPlayingmap() {
		return playingmap;
	}

	public Fighter getFighter() {
		return fighter;
	}

	public void nextMap() {
		this.campaign.getMaps().remove(0);
		initData();
	}
	
	public static boolean saveGame(Game game){
		try{
			FileOutputStream fileOut =
	        new FileOutputStream(Config.GAME_FILE);
	        ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        out.writeObject(game);
	        out.close();
	        fileOut.close();
	        System.out.printf("Serialized data is saved");
	        return true;
		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int getLevel() {
		return fighter.getLevel();
	}
}