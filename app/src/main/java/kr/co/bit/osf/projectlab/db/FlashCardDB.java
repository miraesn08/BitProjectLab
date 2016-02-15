package kr.co.bit.osf.projectlab.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import kr.co.bit.osf.projectlab.dto.BoxDTO;

// http://www.androidhive.info/2013/09/android-sqlite-database-with-multiple-tables/
public class FlashCardDB extends SQLiteOpenHelper {
    private static final String TAG = "FlashCardDBLog";

    // database version & name
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "flashCard.db";

    // http://developer.android.com/intl/ko/training/basics/data-storage/databases.html
    /* Inner class that defines the table contents */
    public static abstract class BoxEntry implements BaseColumns {
        // table name
        public static final String TABLE_NAME = "box";

        // column name
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_SEQ = "seq";

        // table create statement
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
                + "(" + COLUMN_NAME_ID + " INTEGER PRIMARY KEY"
                + "," + COLUMN_NAME_NAME + " TEXT"
                + "," + COLUMN_NAME_TYPE + " INTEGER"
                + "," + COLUMN_NAME_SEQ + " INTEGER"
                + ")";

        // table drop statement
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        // field list
        public static String FIELD_LIST_BOX =  COLUMN_NAME_ID
                + "," + COLUMN_NAME_NAME + "," + COLUMN_NAME_TYPE + "," + COLUMN_NAME_SEQ;
    }

    // table name
    private static final String TABLE_CARD = "card";

    // CARD table column
    private static final String COLUMN_CARD_ID = "id";
    private static final String COLUMN_CARD_NAME = "name";
    private static final String COLUMN_CARD_IMAGE_PATH = "image_path";
    private static final String COLUMN_CARD_TYPE = "type";
    private static final String COLUMN_CARD_SEQ = "seq";
    private static final String COLUMN_CARD_BOX_ID = "box_id";

    // table create statements

    // CARD table create statement
    private static final String CREATE_TABLE_CARD = "CREATE TABLE " + TABLE_CARD
            + "(" + COLUMN_CARD_ID + " INTEGER PRIMARY KEY"
            + "," + COLUMN_CARD_NAME + " TEXT"
            + "," + COLUMN_CARD_IMAGE_PATH + " TEXT"
            + "," + COLUMN_CARD_TYPE + " INTEGER"
            + "," + COLUMN_CARD_SEQ + " INTEGER"
            + "," + COLUMN_CARD_BOX_ID + " INTEGER"
            + ")";

    // CARD table field list
    private static final String FIELD_LIST_CARD = COLUMN_CARD_ID
            + "," + COLUMN_CARD_NAME + "," + COLUMN_CARD_IMAGE_PATH
            + "," + COLUMN_CARD_TYPE + "," + COLUMN_CARD_SEQ + "," + COLUMN_CARD_BOX_ID;


    public FlashCardDB(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public FlashCardDB(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(BoxEntry.CREATE_TABLE);
        db.execSQL(CREATE_TABLE_CARD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL(BoxEntry.DROP_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARD);

        // create new tables
        onCreate(db);
    }

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

    public BoxDTO getBox(String name) {
        BoxDTO box = null;

        String query = "select " + BoxEntry.FIELD_LIST_BOX
                + " from " + BoxEntry.TABLE_NAME
                + " where " + BoxEntry.COLUMN_NAME_NAME + " = \"" + name + "\"";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            box = new BoxDTO();
            box.setId(Integer.parseInt(cursor.getString(0)));
            box.setName(cursor.getString(1));
            box.setType(Integer.parseInt(cursor.getString(2)));
            box.setSeq(Integer.parseInt(cursor.getString(3)));
            cursor.close();
        }

        db.close();

        return box;
    }

    public BoxDTO getBox(int id) {
        BoxDTO box = null;

        String query = "select " + BoxEntry.FIELD_LIST_BOX
                + " from " + BoxEntry.TABLE_NAME
                + " where " + BoxEntry.COLUMN_NAME_ID + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            box = new BoxDTO();
            cursor.moveToFirst();
            box.setId(Integer.parseInt(cursor.getString(0)));
            box.setName(cursor.getString(1));
            box.setType(Integer.parseInt(cursor.getString(2)));
            box.setSeq(Integer.parseInt(cursor.getString(3)));
            cursor.close();
        }

        db.close();

        return box;
    }

    public BoxDTO addBox(BoxDTO box){
        ContentValues values = new ContentValues();
        values.put(BoxEntry.COLUMN_NAME_NAME, box.getName());
        values.put(BoxEntry.COLUMN_NAME_TYPE, box.getType());
        values.put(BoxEntry.COLUMN_NAME_SEQ, box.getSeq());

        SQLiteDatabase db = this.getWritableDatabase();

        // http://developer.android.com/intl/ko/training/basics/data-storage/databases.html#WriteDbRow
        long newRowId;
        newRowId = db.insert(BoxEntry.TABLE_NAME, null, values);
        box.setId(newRowId);
        db.close();

        return box;
    }

    public boolean deleteBox(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // http://developer.android.com/intl/ko/training/basics/data-storage/databases.html#DeleteDbRow
        // Define 'where' part of query.
        String selection = BoxEntry.COLUMN_NAME_ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(id) };
        // Issue SQL statement.
        int count = db.delete(BoxEntry.TABLE_NAME, selection, selectionArgs);
        db.close();

        return (count > 0);
    }

    public boolean updateBox(BoxDTO newValue) {
        SQLiteDatabase db = this.getWritableDatabase();

        // http://developer.android.com/intl/ko/training/basics/data-storage/databases.html#UpdateDbRow
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(BoxEntry.COLUMN_NAME_NAME, newValue.getName());
        values.put(BoxEntry.COLUMN_NAME_TYPE, newValue.getType());
        values.put(BoxEntry.COLUMN_NAME_SEQ, newValue.getSeq());

        // Which row to update, based on the ID
        String selection = BoxEntry.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = { String.valueOf(newValue.getId()) };

        int count = db.update(
                BoxEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        db.close();

        return (count > 0);
    }

}
