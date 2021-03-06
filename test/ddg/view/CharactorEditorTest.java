/**
 * 
 */
package ddg.view;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ddg.Config;
import ddg.model.Fighter;
import ddg.model.FighterModel;
import ddg.model.entity.Chest;
import ddg.model.item.BaseItem;
import ddg.model.item.Enchantment;
import ddg.utils.Utils;

/**
 * This class is to test character editing 
 * 
 * @author Fei Yu
 * @author Zhen Du
 * @date Mar 3, 2017
 */
public class CharactorEditorTest {

	private Fighter fighter;
	/**
	 * This method is set environment
	 * 
	 */
	@Before
	public void setUpBefore() {
//		String g = Utils.readFile(Config.CHARACTER_FILE);
//		FighterModel fighterModel = Utils.fromJson(g, FighterModel.class);
		FighterModel fighterModel = Utils.readObject(Config.CHARACTER_FILE, FighterModel.class);
		if(fighterModel != null){        			
    		try{
        		if( null!=fighterModel.getFighters() ){
        			HashMap<String, Fighter> map = fighterModel.getFighters();
                    Set<String> keySet1 = map.keySet();
                    Iterator<String> it1 = keySet1.iterator();
                    
                    if (it1.hasNext()){
                    	String keyName = it1.next();
                    	fighter = map.get(keyName);
                    }
        		}
    		}
    		catch (NullPointerException ex){
    			System.out.println("there is a NullPointerException");
    		}        			
		}
	}

	/**
	 * This method tests if wearing a item can influence the character's ability correctly.
	 */
	@Test
	public void testAbility() {
		BaseItem i = new BaseItem(BaseItem.RING, 3, Enchantment.STRENGTH);
		int init = fighter.getTotalStrength();

		fighter.wearItem(i, false);
		
		int after = fighter.getTotalStrength();
		assertTrue(after-init==3);
	}

	/**
	 * This method tests if a character can only take one item for one type.
	 */
	@Test
	public void testWearingItem() {
		int n = fighter.getWorn().size();
		BaseItem i = new BaseItem(BaseItem.RING, 3, Enchantment.STRENGTH);
		i.setBonus(3);

		fighter.wearItem(i, false);
		fighter.wearItem(i, false);
		fighter.wearItem(i, false);
		assertTrue(fighter.getWorn().size()-n ==1 || fighter.getWorn().size()-n == 0);
	}

	/**
	 * This method tests the character opening a chest
	 */
	@Test
	public void testOpenChest(){
		int n = fighter.getBackpack().size();
		BaseItem i = new BaseItem(BaseItem.HELMET);
		Chest chest = new Chest(i);
		fighter.openChest(chest);
		assertTrue(fighter.getBackpack().size() == n+1);
	}

	/**
	 * This method tests the character looting a dead npc.
	 */
	@Test
	public void testLootCorpseItem(){
		int n = fighter.getBackpack().size();
		Fighter corpse = new Fighter();
		BaseItem i = new BaseItem(BaseItem.HELMET);
		corpse.getBackpack().add(i);
		corpse.die();
		fighter.lootCorpseItems(corpse);
		assertTrue(fighter.getBackpack().size() == n + 1);
	}
}
