package kr.co.bit.osf.projectlab.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import kr.co.bit.osf.projectlab.R;

// http://www.androidhive.info/2013/09/android-sqlite-database-with-multiple-tables/
public class FlashCardDB extends SQLiteOpenHelper implements BoxDAO, CardDAO, StateDAO {
    private static final String TAG = "FlashCardDBLog";

    // context
    Context context = null;

    // database version & name
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FlashCard.db";

    // http://developer.android.com/intl/ko/training/basics/data-storage/databases.html
    /* Inner class that defines the table contents */
    public static abstract class BoxEntry implements BaseColumns {
        // table name
        public static final String TABLE_NAME = "box";

        // column name
        public static final String COLUMN_NAME_ENTRY_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_SEQ = "seq";

        // type
        public static final int TYPE_USER = 0;
        public static final int TYPE_DEMO = 1;

        // table create statement
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
                + "(" + COLUMN_NAME_ENTRY_ID + " INTEGER PRIMARY KEY"
                + "," + COLUMN_NAME_NAME + " TEXT"
                + "," + COLUMN_NAME_TYPE + " INTEGER"
                + "," + COLUMN_NAME_SEQ + " INTEGER"
                + ")";

        // table drop statement
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        // field list
        public static final String FIELD_LIST =  COLUMN_NAME_ENTRY_ID
                + "," + COLUMN_NAME_NAME + "," + COLUMN_NAME_TYPE + "," + COLUMN_NAME_SEQ;

        // column id
        public static final int COLUMN_ID_ENTRY_ID = 0;
        public static final int COLUMN_ID_NAME = 1;
        public static final int COLUMN_ID_TYPE = 2;
        public static final int COLUMN_ID_SEQ = 3;
    }

    public static abstract class CardEntry implements BaseColumns {
        // table name
        public static final String TABLE_NAME = "card";

        // column name
        public static final String COLUMN_NAME_ENTRY_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_IMAGE_PATH = "image_path";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_SEQ = "seq";
        public static final String COLUMN_NAME_BOX_ID = "box_id";

        // type
        public static final int TYPE_USER = 0;
        public static final int TYPE_DEMO = 1;

        // table create statement
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
                + "(" + COLUMN_NAME_ENTRY_ID + " INTEGER PRIMARY KEY"
                + "," + COLUMN_NAME_NAME + " TEXT"
                + "," + COLUMN_NAME_IMAGE_PATH + " TEXT"
                + "," + COLUMN_NAME_TYPE + " INTEGER"
                + "," + COLUMN_NAME_SEQ + " INTEGER"
                + "," + COLUMN_NAME_BOX_ID + " INTEGER"
                + ")";

        // table drop statement
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        // field list
        public static final String FIELD_LIST =  COLUMN_NAME_ENTRY_ID
                + "," + COLUMN_NAME_NAME + "," + COLUMN_NAME_IMAGE_PATH
                + "," + COLUMN_NAME_TYPE + "," + COLUMN_NAME_SEQ
                + "," + COLUMN_NAME_BOX_ID;

        // column id
        public static final int COLUMN_ID_ENTRY_ID = 0;
        public static final int COLUMN_ID_NAME = 1;
        public static final int COLUMN_ID_IMAGE_PATH = 2;
        public static final int COLUMN_ID_TYPE = 3;
        public static final int COLUMN_ID_SEQ = 4;
        public static final int COLUMN_ID_BOX_ID = 5;
    }

    public static abstract class StateEntry implements BaseColumns {
        // table name
        public static final String TABLE_NAME = "state";

        // column name
        public static final String COLUMN_NAME_ENTRY_ID = "id";
        public static final String COLUMN_NAME_BOX_ID = "box_id";
        public static final String COLUMN_NAME_CARD_ID = "card_id";

        // table create statement
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
                + "(" + COLUMN_NAME_ENTRY_ID + " INTEGER PRIMARY KEY"
                + "," + COLUMN_NAME_BOX_ID + " INTEGER"
                + "," + COLUMN_NAME_CARD_ID + " INTEGER"
                + ")";

        // table drop statement
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        // field list
        public static final String FIELD_LIST =  COLUMN_NAME_ENTRY_ID
                + "," + COLUMN_NAME_BOX_ID+ "," + COLUMN_NAME_CARD_ID;

        // column id
        public static final int COLUMN_ID_ENTRY_ID = 0;
        public static final int COLUMN_ID_BOX_ID = 1;
        public static final int COLUMN_ID_CARD_ID = 2;
    }

    public FlashCardDB(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public FlashCardDB(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(BoxEntry.CREATE_TABLE);
        db.execSQL(CardEntry.CREATE_TABLE);
        db.execSQL(StateEntry.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL(BoxEntry.DROP_TABLE);
        db.execSQL(CardEntry.DROP_TABLE);
        db.execSQL(StateEntry.DROP_TABLE);

        // create new tables
        onCreate(db);
    }

    // box
    @Override
    public BoxDTO addBox(String name){
        ContentValues values = new ContentValues();
        values.put(BoxEntry.COLUMN_NAME_NAME, name);
        values.put(BoxEntry.COLUMN_NAME_TYPE, 0);
        values.put(BoxEntry.COLUMN_NAME_SEQ, 0);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(BoxEntry.TABLE_NAME, null, values);
        db.close();

        return getBox(name);
    }

    @Override
    public BoxDTO getBox(String name) {
        BoxDTO box = null;

        String query = "select " + BoxEntry.FIELD_LIST
                + " from " + BoxEntry.TABLE_NAME
                + " where " + BoxEntry.COLUMN_NAME_NAME + " = \"" + name + "\"";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                box = new BoxDTO();
                box.setId(Integer.parseInt(cursor.getString(BoxEntry.COLUMN_ID_ENTRY_ID)));
                box.setName(cursor.getString(BoxEntry.COLUMN_ID_NAME));
                box.setType(Integer.parseInt(cursor.getString(BoxEntry.COLUMN_ID_TYPE)));
                box.setSeq(Integer.parseInt(cursor.getString(BoxEntry.COLUMN_ID_SEQ)));
            }
            cursor.close();
        }

        db.close();

        return box;
    }

    @Override
    public BoxDTO getBox(int id) {
        BoxDTO box = null;

        String query = "select " + BoxEntry.FIELD_LIST
                + " from " + BoxEntry.TABLE_NAME
                + " where " + BoxEntry.COLUMN_NAME_ENTRY_ID + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                box = new BoxDTO();
                box.setId(Integer.parseInt(cursor.getString(BoxEntry.COLUMN_ID_ENTRY_ID)));
                box.setName(cursor.getString(BoxEntry.COLUMN_ID_NAME));
                box.setType(Integer.parseInt(cursor.getString(BoxEntry.COLUMN_ID_TYPE)));
                box.setSeq(Integer.parseInt(cursor.getString(BoxEntry.COLUMN_ID_SEQ)));
            }
            cursor.close();
        }

        db.close();

        return box;
    }

    @Override
    public boolean addBox(BoxDTO box) {
        ContentValues values = new ContentValues();
        values.put(BoxEntry.COLUMN_NAME_NAME, box.getName());
        values.put(BoxEntry.COLUMN_NAME_TYPE, box.getType());
        values.put(BoxEntry.COLUMN_NAME_SEQ, box.getSeq());

        SQLiteDatabase db = this.getWritableDatabase();

        // http://developer.android.com/intl/ko/training/basics/data-storage/databases.html#WriteDbRow
        long newRowId = db.insert(BoxEntry.TABLE_NAME, null, values);
        box.setId(newRowId);
        db.close();

        return (newRowId > 0);
    }

    @Override
    public boolean deleteBox(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // http://developer.android.com/intl/ko/training/basics/data-storage/databases.html#DeleteDbRow
        // Define 'where' part of query.
        String selection = BoxEntry.COLUMN_NAME_ENTRY_ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(id)};
        // Issue SQL statement.
        int count = db.delete(BoxEntry.TABLE_NAME, selection, selectionArgs);
        db.close();

        return (count > 0);
    }

    @Override
    public boolean updateBox(BoxDTO newValue) {
        SQLiteDatabase db = this.getWritableDatabase();

        // http://developer.android.com/intl/ko/training/basics/data-storage/databases.html#UpdateDbRow
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(BoxEntry.COLUMN_NAME_NAME, newValue.getName());
        values.put(BoxEntry.COLUMN_NAME_TYPE, newValue.getType());
        values.put(BoxEntry.COLUMN_NAME_SEQ, newValue.getSeq());

        // Which row to update, based on the ID
        String selection = BoxEntry.COLUMN_NAME_ENTRY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(newValue.getId())};

        int count = db.update(
                BoxEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        db.close();

        return (count > 0);
    }

    @Override
    public List<BoxDTO> getBoxAll() {
        List<BoxDTO> list = new ArrayList<>();

        String query = "select " + BoxEntry.FIELD_LIST
                + " from " + BoxEntry.TABLE_NAME
                + " order by " + BoxEntry.COLUMN_NAME_SEQ;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                BoxDTO box = new BoxDTO();
                box.setId(Integer.parseInt(cursor.getString(BoxEntry.COLUMN_ID_ENTRY_ID)));
                box.setName(cursor.getString(BoxEntry.COLUMN_ID_NAME));
                box.setType(Integer.parseInt(cursor.getString(BoxEntry.COLUMN_ID_TYPE)));
                box.setSeq(Integer.parseInt(cursor.getString(BoxEntry.COLUMN_ID_SEQ)));

                list.add(box);
            }
            cursor.close();
        }

        db.close();

        return list;
    }

    // card
    @Override
    public boolean addCard(CardDTO card) {
        ContentValues values = new ContentValues();
        values.put(CardEntry.COLUMN_NAME_NAME, card.getName());
        values.put(CardEntry.COLUMN_NAME_IMAGE_PATH, card.getImagePath());
        values.put(CardEntry.COLUMN_NAME_TYPE, card.getType());
        values.put(CardEntry.COLUMN_NAME_SEQ, card.getSeq());
        values.put(CardEntry.COLUMN_NAME_BOX_ID, card.getBoxId());

        SQLiteDatabase db = this.getWritableDatabase();
        long newRowId = db.insert(CardEntry.TABLE_NAME, null, values);
        card.setId(newRowId);
        db.close();

        return (newRowId > 0);
    }

    @Override
    public CardDTO getCard(int id) {
        CardDTO card = null;

        String query = "select " + CardEntry.FIELD_LIST
                + " from " + CardEntry.TABLE_NAME
                + " where " + CardEntry.COLUMN_NAME_ENTRY_ID + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                card = new CardDTO();
                card.setId(Integer.parseInt(cursor.getString(CardEntry.COLUMN_ID_ENTRY_ID)));
                card.setName(cursor.getString(CardEntry.COLUMN_ID_NAME));
                card.setImagePath(cursor.getString(CardEntry.COLUMN_ID_IMAGE_PATH));
                card.setType(Integer.parseInt(cursor.getString(CardEntry.COLUMN_ID_TYPE)));
                card.setSeq(Integer.parseInt(cursor.getString(CardEntry.COLUMN_ID_SEQ)));
                card.setBoxId(Integer.parseInt(cursor.getString(CardEntry.COLUMN_ID_BOX_ID)));
            }
            cursor.close();
        }

        db.close();

        return card;
    }

    @Override
    public boolean deleteCard(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = CardEntry.COLUMN_NAME_ENTRY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        int count = db.delete(CardEntry.TABLE_NAME, selection, selectionArgs);
        db.close();

        return (count > 0);
    }

    @Override
    public boolean updateCard(CardDTO newValue) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CardEntry.COLUMN_NAME_NAME, newValue.getName());
        values.put(CardEntry.COLUMN_NAME_IMAGE_PATH, newValue.getImagePath());
        values.put(CardEntry.COLUMN_NAME_TYPE, newValue.getType());
        values.put(CardEntry.COLUMN_NAME_SEQ, newValue.getSeq());
        values.put(CardEntry.COLUMN_NAME_BOX_ID, newValue.getBoxId());

        String selection = CardEntry.COLUMN_NAME_ENTRY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(newValue.getId())};

        int count = db.update(
                CardEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        db.close();

        return (count > 0);
    }

    @Override
    public List<CardDTO> getCardByBoxId(int boxId) {
        List<CardDTO> list = new ArrayList<>();

        String query = "select " + CardEntry.FIELD_LIST
                + " from " + CardEntry.TABLE_NAME
                + " where " + CardEntry.COLUMN_NAME_BOX_ID + " = " + boxId
                + " order by " + CardEntry.COLUMN_NAME_SEQ;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                CardDTO card = new CardDTO();
                card.setId(Integer.parseInt(cursor.getString(CardEntry.COLUMN_ID_ENTRY_ID)));
                card.setName(cursor.getString(CardEntry.COLUMN_ID_NAME));
                card.setImagePath(cursor.getString(CardEntry.COLUMN_ID_IMAGE_PATH));
                card.setType(Integer.parseInt(cursor.getString(CardEntry.COLUMN_ID_TYPE)));
                card.setSeq(Integer.parseInt(cursor.getString(CardEntry.COLUMN_ID_SEQ)));
                card.setBoxId(Integer.parseInt(cursor.getString(CardEntry.COLUMN_ID_BOX_ID)));

                list.add(card);
            }
            cursor.close();
        }

        db.close();

        return list;
    }

    @Override
    public boolean deleteCardByBoxId(int boxId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = CardEntry.COLUMN_NAME_BOX_ID + " = ?";
        String[] selectionArgs = {String.valueOf(boxId)};
        int count = db.delete(CardEntry.TABLE_NAME, selection, selectionArgs);
        db.close();

        return (count > 0);
    }

    // state
    @Override
    public boolean addState(int boxId, int cardId) {
        ContentValues values = new ContentValues();
        values.put(StateEntry.COLUMN_NAME_BOX_ID, boxId);
        values.put(StateEntry.COLUMN_NAME_CARD_ID, cardId);

        SQLiteDatabase db = this.getWritableDatabase();
        long newRowId = db.insert(StateEntry.TABLE_NAME, null, values);
        db.close();

        return (newRowId > 0);
    }

    @Override
    public StateDTO getState() {
        return getState(1);
    }

    @Override
    public StateDTO getState(int id) {
        StateDTO state = null;

        String query = "select " + StateEntry.FIELD_LIST
                + " from " + StateEntry.TABLE_NAME
                + " where " + StateEntry.COLUMN_NAME_ENTRY_ID + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                state = new StateDTO();
                state.setId(Integer.parseInt(cursor.getString(StateEntry.COLUMN_ID_ENTRY_ID)));
                state.setBoxId(Integer.parseInt(cursor.getString(StateEntry.COLUMN_ID_BOX_ID)));
                state.setCardId(Integer.parseInt(cursor.getString(StateEntry.COLUMN_ID_CARD_ID)));
            }
            cursor.close();
        }

        db.close();

        return state;
    }

    @Override
    public boolean deleteState(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = StateEntry.COLUMN_NAME_ENTRY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        int count = db.delete(StateEntry.TABLE_NAME, selection, selectionArgs);
        db.close();

        return (count > 0);
    }

    @Override
    public boolean updateState(StateDTO newValue) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(StateEntry.COLUMN_NAME_BOX_ID, newValue.getBoxId());
        values.put(StateEntry.COLUMN_NAME_CARD_ID, newValue.getCardId());

        String selection = StateEntry.COLUMN_NAME_ENTRY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(newValue.getId())};

        int count = db.update(
                StateEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        db.close();

        return (count > 0);
    }

    // demo data
    public boolean createBoxDemoData() {
        BoxDTO demoBox = new BoxDTO(0,
                context.getString(R.string.box_demo_data_name),
                BoxEntry.TYPE_DEMO,
                1);
        return addBox(demoBox);
    }
    public boolean createCardDemoData() {
        CardDTO[] demoList = {
                new CardDTO(0,
                        context.getString(R.string.card_demo_data1_name),
                        context.getString(R.string.card_demo_data1_image_name),
                        CardEntry.TYPE_DEMO, 1, 1),
                new CardDTO(0,
                        context.getString(R.string.card_demo_data2_name),
                        context.getString(R.string.card_demo_data2_image_name),
                        CardEntry.TYPE_DEMO, 2, 1),
                new CardDTO(0,
                        context.getString(R.string.card_demo_data3_name),
                        context.getString(R.string.card_demo_data3_image_name),
                        CardEntry.TYPE_DEMO, 3, 1)
        };

        return (addCard(demoList[0]) && addCard(demoList[1]) && addCard(demoList[2]));
    }

    // initialize db
    public boolean initialize() {
        return createBoxDemoData()
                && createCardDemoData()
                && addState(1, 1);
    }

}
