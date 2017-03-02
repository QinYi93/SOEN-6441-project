package ddg.view;

import ddg.Config;
import ddg.model.MapEditorModel;
import ddg.utils.Utils;
import junit.framework.TestCase;
import model.Map;
import ddg.view.MapEditor;
import model.ValidationTool;

/**
 * This class is test for MapEditor
 *
 * @author Qin yi
 * @date Mar 1, 2017
 */
public class MapEditorTest extends TestCase {
    private MapEditorModel mapModel;
    private Map map;
    private ValidationTool validationTool;

    public void setUp() throws Exception {
        super.setUp();
        mapModel = new MapEditorModel();
        String g = Utils.readFile(Config.MAP_FILE);
        mapModel = Utils.fromJson(g, MapEditorModel.class);
        map = mapModel.getMapByIndex(1);
        validationTool = new ValidationTool(map);

    }

    public void testHasValidPath() throws Exception {
        for (int i = 0; i < map.getRow() ; i ++)
            for (int j =0; j < map.getColumn(); j++ ){
                if(map.getLocation()[i][j] == 'i'){
                    validationTool.hasValidPath(i , j);
                    assertTrue(validationTool.isHasvaildpath());
                }

            }

    }

    public void testHasEntryDoor() throws Exception {
        assertTrue(validationTool.hasEntryDoor());
    }

    public void testHasExitDoor() throws Exception {
        assertTrue(validationTool.hasExitDoor());
    }

}