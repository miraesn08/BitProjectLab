package kr.co.bit.osf.projectlab;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.bit.osf.projectlab.db.BoxDAO;
import kr.co.bit.osf.projectlab.db.BoxDTO;
import kr.co.bit.osf.projectlab.db.CardDAO;
import kr.co.bit.osf.projectlab.db.CardDTO;
import kr.co.bit.osf.projectlab.db.FlashCardDB;
import kr.co.bit.osf.projectlab.db.StateDAO;
import kr.co.bit.osf.projectlab.db.StateDTO;

// http://stackoverflow.com/questions/8499554/android-junit-test-for-sqliteopenhelper
public class FlashCardDBTest extends AndroidTestCase {
    private FlashCardDB db;
    private BoxDAO boxDao;
    private CardDAO cardDao;
    private StateDAO stateDao;

    private Context context = null;

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
        this.context = context;
        db = new FlashCardDB(context);
        boxDao = db;
        cardDao = db;
        stateDao = db;
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }

    // box
    private void setupBoxData() {
        for (BoxDTO box : boxDataList) {
            boxDao.addBox(box);
        }
    }

    public void testAddBox() throws Exception {
        int addIndex = 2;
        BoxDTO box = boxDataList[addIndex];

        assertEquals(true, boxDao.addBox(box));
        assertEquals(true, (box.getId() == 1));
        assertEquals(true, boxDataList[addIndex].equals(box));
    }

    public void testAddBoxByName() throws Exception {
        String name = "animal";
        BoxDTO addedBox = boxDao.addBox(name);

        assertNotNull(addedBox);
        assertEquals(true, (addedBox.getId() == 1));
        assertEquals(true, name.equals(addedBox.getName()));
    }

    public void testGetBoxById() throws Exception {
        setupBoxData();
        int findIndex = 1;
        String findName = boxDataList[findIndex].getName();

        BoxDTO box = boxDao.getBox(findIndex+1);

        assertNotNull(box);
        assertEquals(true, (box.getId() == findIndex+1));
        assertEquals(true, (box.getName().equals(findName)));

        box = boxDao.getBox(9999);
        assertNull(box);
    }

    public void testGetBoxByName() throws Exception {
        setupBoxData();
        int findIndex = 2;
        BoxDTO box = boxDao.getBox(boxDataList[findIndex].getName());

        assertNotNull(box);
        assertEquals(true, (box.getId() == findIndex + 1));
        assertEquals(true, (box.getType() == boxDataList[findIndex].getType()));
        assertEquals(true, (box.getName().equals(boxDataList[findIndex].getName())));

        box = boxDao.getBox("not found!");
        assertNull(box);
    }

    public void testDeleteBox() throws Exception {
        setupBoxData();
        int deleteId = 2;

        assertEquals(true, (boxDao.deleteBox(deleteId)));
        assertNull(boxDao.getBox(deleteId));
    }

    public void testUpdateBox() throws Exception {
        setupBoxData();
        int updateId = 2;

        BoxDTO newValue = new BoxDTO(updateId, "new value", updateId+1, updateId+2);
        assertEquals(true, (boxDao.updateBox(newValue)));

        BoxDTO updatedValue = boxDao.getBox(updateId);
        assertNotNull(updatedValue);
        assertEquals(true, (newValue.equals(updatedValue)));
        assertEquals(true, (newValue.getType() == updatedValue.getType()));
        assertEquals(true, (newValue.getSeq() == updatedValue.getSeq()));
    }

    // card
    private void setupCardData() {
        for (CardDTO card : cardDataList) {
            cardDao.addCard(card);
        }
    }

    public void testAddCard() throws Exception {
        int addIndex = 2;
        CardDTO card = cardDataList[addIndex];

        assertEquals(true, cardDao.addCard(card));
        assertEquals(true, (card.getId() == 1));
        assertEquals(true, cardDataList[addIndex].equals(card));
    }

    public void testGetCardById() throws Exception {
        setupCardData();
        int findIndex = 2;

        CardDTO card = cardDao.getCard(findIndex + 1);

        assertNotNull(card);
        assertEquals(true, (card.getId() == findIndex+1));
        assertEquals(true, (cardDataList[findIndex].equals(card)));

        card = cardDao.getCard(9999);
        assertNull(card);
    }

    public void testDeleteCard() throws Exception {
        setupCardData();
        int deleteId = 1;

        assertEquals(true, (cardDao.deleteCard(deleteId)));
        assertNull(cardDao.getCard(deleteId));
    }

    public void testUpdateCard() throws Exception {
        setupCardData();
        int updateId = 1;

        CardDTO newValue = new CardDTO(updateId, "new name", "new image path", updateId+1, updateId+2);
        assertEquals(true, (cardDao.updateCard(newValue)));

        CardDTO updatedValue = cardDao.getCard(updateId);
        assertNotNull(updatedValue);
        assertEquals(true, (newValue.equals(updatedValue)));
    }

    public void testGetCardByBoxId() throws Exception {
        setupCardData();
        int findBoxId = 1;

        List<CardDTO> list = cardDao.getCardByBoxId(findBoxId);
        assertEquals(true, (list.size() > 0));

        Map<String, CardDTO> map = new HashMap<>();
        for (CardDTO card : list) {
            map.put(card.getName(), card);
        }
        //
        boolean isNotFound = false;
        for(CardDTO card : cardDataList) {
            if (map.get(card.getName()) == null) {
                isNotFound = true;
                break;
            }
        }
        //
        assertEquals(false, isNotFound);

        list = cardDao.getCardByBoxId(9999);
        assertEquals(true, (list.size() == 0));
    }

    // state
    private void setupStateData() {
        stateDao.addState(1, 1);
    }

    public void testAddState() throws Exception {
        int boxId = 1;
        int cardId = 1;

        assertEquals(true, stateDao.addState(boxId, cardId));
    }

    public void testGetState() throws Exception {
        setupStateData();

        StateDTO state = stateDao.getState();

        assertNotNull(state);
        assertEquals(true, (state.getBoxId() == 1));
        assertEquals(true, (state.getCardId() == 1));

        state = stateDao.getState(9999);
        assertNull(state);
    }

    public void testDeleteState() throws Exception {
        setupStateData();
        int deleteId = 1;

        assertEquals(true, (stateDao.deleteState(deleteId)));
        assertNull(stateDao.getState(deleteId));
    }

    public void testUpdateState() throws Exception {
        setupStateData();
        int updateId = 1;

        StateDTO newValue = new StateDTO(updateId, updateId+1, updateId+2);
        assertEquals(true, (stateDao.updateState(newValue)));

        StateDTO updatedValue = stateDao.getState(updateId);
        assertNotNull(updatedValue);
        assertEquals(true, (newValue.equals(updatedValue)));
    }

    // demo data
    public void testCreateBoxDemoData() throws Exception {
        assertEquals(true, db.createBoxDemoData());
        //assertEquals(true, "동물".equals(context.getString(R.string.box_demo_data_name)));
        //assertEquals(true, "동물".equals(db.getBox(1).getName()));
        BoxDTO box = boxDao.getBox(1);
        assertEquals(true, context.getString(R.string.box_demo_data_name).equals(box.getName()));
        assertEquals(true, (box.getType() == FlashCardDB.BoxEntry.TYPE_DEMO));
        assertEquals(true, (box.getSeq() == 1));
    }

    public void testCreateCardDemoData() throws Exception {
        assertEquals(true, db.createCardDemoData());

        CardDTO card = cardDao.getCard(3);
        assertEquals(true, context.getString(R.string.card_demo_data3_name).equals(card.getName()));
        assertEquals(true, context.getString(R.string.card_demo_data3_image_name).equals(card.getImagePath()));
        assertEquals(true, (card.getType() == FlashCardDB.CardEntry.TYPE_DEMO));
        assertEquals(true, (card.getBoxId() == 1));
    }

    // initialize
    public void testInitialize() throws Exception {
        assertEquals(true, db.initialize());

        List<BoxDTO> boxList = boxDao.getBoxAll();
        assertEquals(true, (boxList.size() == 1));

        BoxDTO box = boxDao.getBox(1);
        assertEquals(true, context.getString(R.string.box_demo_data_name).equals(box.getName()));
        assertEquals(true, (box.getType() == FlashCardDB.BoxEntry.TYPE_DEMO));
        assertEquals(true, (box.getSeq() == 1));

        List<CardDTO> cardList = cardDao.getCardByBoxId(1);
        assertEquals(true, (cardList.size() == 3));

        CardDTO card = cardDao.getCard(3);
        assertEquals(true, context.getString(R.string.card_demo_data3_name).equals(card.getName()));
        assertEquals(true, context.getString(R.string.card_demo_data3_image_name).equals(card.getImagePath()));
        assertEquals(true, (card.getType() == FlashCardDB.CardEntry.TYPE_DEMO));
        assertEquals(true, (card.getBoxId() == 1));

        StateDTO state = stateDao.getState();
        assertNotNull(state);
        assertEquals(true, (state.getBoxId() == 1));
        assertEquals(true, (state.getCardId() == 1));
    }

}
