package kr.co.bit.osf.projectlab;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.bit.osf.projectlab.db.FlashCardDB;
import kr.co.bit.osf.projectlab.dto.BoxDTO;
import kr.co.bit.osf.projectlab.dto.CardDTO;

// http://stackoverflow.com/questions/8499554/android-junit-test-for-sqliteopenhelper
public class FlashCardDBTest extends AndroidTestCase {
    private FlashCardDB db;

    // box test data list
    BoxDTO[] boxDataList = {
            new BoxDTO(1, "animal", 1, 1),
            new BoxDTO(2, "food", 2, 2),
            new BoxDTO(3, "playground", 3, 3)
    };

    // card test data list
    CardDTO[] cardDataList = {
            new CardDTO("dog","dog", FlashCardDB.CardEntry.TYPE_DEMO, 1),
            new CardDTO("cat", "cat", FlashCardDB.CardEntry.TYPE_DEMO, 1),
            new CardDTO("rabbit", "rabbit", FlashCardDB.CardEntry.TYPE_DEMO, 1)
    };

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        db = new FlashCardDB(context);
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }

    // box
    private void setupBoxData() {
        for(int i = 0; i < boxDataList.length; i++) {
            db.addBox(boxDataList[i]);
        }
    }

    public void testAddBoxByName() throws Exception {
        String boxName = "animal";
        BoxDTO addedBox = db.addBox(boxName);

        assertEquals(true, (addedBox != null));
        assertEquals(true, boxName.equals(addedBox.getName()));
    }

    public void testGetBoxById() throws Exception {
        setupBoxData();
        int findIndex = 1;
        String findName = boxDataList[findIndex].getName();

        BoxDTO box = db.getBox(findIndex+1);

        assertEquals(true, (box != null));
        assertEquals(true, (box.getId() == findIndex+1));
        assertEquals(true, (box.getName().equals(findName)));

        box = db.getBox(9999);
        assertEquals(true, (box == null));
    }

    public void testGetBoxByName() throws Exception {
        setupBoxData();
        int findIndex = 2;
        BoxDTO box = db.getBox(boxDataList[findIndex].getName());

        assertEquals(true, (box != null));
        assertEquals(true, (box.getId() == findIndex+1));
        assertEquals(true, (box.getType() == boxDataList[findIndex].getType()));
        assertEquals(true, (box.getName().equals(boxDataList[findIndex].getName())));

        box = db.getBox("not found!");
        assertEquals(true, (box == null));
    }

    public void testDeleteBox() throws Exception {
        setupBoxData();
        int deleteId = 2;

        assertEquals(true, (db.deleteBox(deleteId)));
        assertEquals(true, (db.getBox(deleteId) == null));
    }

    public void testUpdateBox() throws Exception {
        setupBoxData();
        int updateId = 2;

        BoxDTO newValue = new BoxDTO(updateId, "new value", updateId+1, updateId+2);
        assertEquals(true, (db.updateBox(newValue)));

        BoxDTO updatedValue = db.getBox(updateId);
        assertEquals(true, (updatedValue != null));
        assertEquals(true, (newValue.equals(updatedValue)));
        assertEquals(true, (newValue.getType() == updatedValue.getType()));
        assertEquals(true, (newValue.getSeq() == updatedValue.getSeq()));
    }

    // card
    private void setupCardData() {
        for(int i = 0; i < cardDataList.length; i++) {
            db.addCard(cardDataList[i]);
        }
    }

    public void testAddCard() throws Exception {
        int addIndex = 2;
        CardDTO card = db.addCard(cardDataList[addIndex]);

        assertEquals(true, (card.getId() > 0));
        assertEquals(true, cardDataList[addIndex].equals(card));
    }

    public void testGetCardById() throws Exception {
        setupCardData();
        int findIndex = 2;

        CardDTO card = db.getCard(findIndex + 1);

        assertEquals(true, (card != null));
        assertEquals(true, (card.getId() == findIndex+1));
        assertEquals(true, (cardDataList[findIndex].equals(card)));

        card = db.getCard(9999);
        assertEquals(true, (card == null));
    }

    public void testDeleteCard() throws Exception {
        setupCardData();
        int deleteId = 1;

        assertEquals(true, (db.deleteCard(deleteId)));
        assertEquals(true, (db.getCard(deleteId) == null));
    }

    public void testUpdateCard() throws Exception {
        setupCardData();
        int updateId = 1;

        CardDTO newValue = new CardDTO(updateId, "new name", "new image path", updateId+1, updateId+2);
        assertEquals(true, (db.updateCard(newValue)));

        CardDTO updatedValue = db.getCard(updateId);
        assertEquals(true, (updatedValue != null));
        assertEquals(true, (newValue.equals(updatedValue)));
    }

    public void testGetCardByBoxId() throws Exception {
        setupCardData();
        int findBoxId = 1;

        List<CardDTO> list = db.getCardByBoxId(findBoxId);
        assertEquals(true, (list.size() > 0));

        if (list != null) {
            Map<String, CardDTO> map = new HashMap<>();
            for (int i = 0 ; i < list.size(); i++) {
                map.put(list.get(i).getName(), list.get(i));
            }
            //
            boolean isNotFound = false;
            for(int i = 0; i < cardDataList.length; i++) {
                if (map.get(cardDataList[i].getName()) == null) {
                    isNotFound = true;
                    break;
                }
            }
            //
            assertEquals(false, isNotFound);
        }

        list = db.getCardByBoxId(9999);
        assertEquals(true, (list.size() == 0));
    }
}
