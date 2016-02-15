package kr.co.bit.osf.projectlab;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import kr.co.bit.osf.projectlab.db.FlashCardDB;
import kr.co.bit.osf.projectlab.dto.BoxDTO;
import kr.co.bit.osf.projectlab.dto.CardDTO;

// http://stackoverflow.com/questions/8499554/android-junit-test-for-sqliteopenhelper
public class FlashCardDBTest extends AndroidTestCase {
    private FlashCardDB db;

    // box test data list
    BoxDTO[] boxList = {
            new BoxDTO(1, "animal", 1, 1),
            new BoxDTO(2, "food", 2, 2),
            new BoxDTO(3, "playground", 3, 3)
    };

    // card test data list
    CardDTO[] cardList = {
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
    public void testAddBoxByName() throws Exception {
        String boxName = "animal";
        BoxDTO addedBox = db.addBox(boxName);

        assertEquals(true, (addedBox != null));
        assertEquals(true, boxName.equals(addedBox.getName()));
    }

    public void testGetBoxById() throws Exception {
        int findId = 3;
        String findName = "rabbit";

        db.addBox("dog");
        db.addBox("cat");
        db.addBox(findName);

        BoxDTO box = db.getBox(findId);

        assertEquals(true, (box != null));
        assertEquals(true, (box.getId() == findId));
        assertEquals(true, (box.getName().equals(findName)));
    }

    public void testGetBoxByName() throws Exception {
        for(int i = 0; i < boxList.length; i++) {
            db.addBox(boxList[i]);
        }
        int findIndex = 2;
        BoxDTO box = db.getBox(boxList[findIndex].getName());

        assertEquals(true, (box != null));
        assertEquals(true, (box.getId() == findIndex+1));
        assertEquals(true, (box.getType() == boxList[findIndex].getType()));
        assertEquals(true, (box.getName().equals(boxList[findIndex].getName())));
    }

    public void testDeleteBox() throws Exception {
        for(int i = 0; i < boxList.length; i++) {
            db.addBox(boxList[i]);
        }
        int deleteId = 2;

        assertEquals(true, (db.deleteBox(deleteId)));
        assertEquals(true, (db.getBox(deleteId) == null));
    }

    public void testUpdateBox() throws Exception {
        for(int i = 0; i < boxList.length; i++) {
            db.addBox(boxList[i]);
        }
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
    public void testAddCard() throws Exception {
        int addIndex = 2;
        CardDTO card = db.addCard(cardList[addIndex]);

        assertEquals(true, (card.getId() > 0));
        assertEquals(true, cardList[addIndex].equals(card));
    }

    public void testGetCardById() throws Exception {
        for(int i = 0; i < cardList.length; i++) {
            db.addCard(cardList[i]);
        }
        int findIndex = 2;

        CardDTO card = db.getCard(findIndex+1);

        assertEquals(true, (card != null));
        assertEquals(true, (card.getId() == findIndex+1));
        assertEquals(true, (cardList[findIndex].equals(card)));
    }
}
