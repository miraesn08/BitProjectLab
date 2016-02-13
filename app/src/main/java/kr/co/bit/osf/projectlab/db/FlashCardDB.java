package kr.co.bit.osf.projectlab.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import kr.co.bit.osf.projectlab.dto.BoxDTO;

// http://www.androidhive.info/2013/09/android-sqlite-database-with-multiple-tables/
public class FlashCardDB extends SQLiteOpenHelper {
    private static final String TAG = "FlashCardDBLog";

    // database version & name
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "flashCard.db";

    // table name
    private static final String TABLE_BOX = "box";
    private static final String TABLE_CARD = "card";

    // BOX table column
    private static final String COLUMN_BOX_ID = "id";
    private static final String COLUMN_BOX_NAME = "name";
    private static final String COLUMN_BOX_TYPE = "type";
    private static final String COLUMN_BOX_SEQ = "seq";

    // CARD table column
    private static final String COLUMN_CARD_ID = "id";
    private static final String COLUMN_CARD_NAME = "name";
    private static final String COLUMN_CARD_IMAGE_PATH = "image_path";
    private static final String COLUMN_CARD_TYPE = "type";
    private static final String COLUMN_CARD_SEQ = "seq";
    private static final String COLUMN_CARD_BOX_ID = "box_id";

    // table create statements
    // BOX table create statement
    private static final String CREATE_TABLE_BOX = "CREATE TABLE " + TABLE_BOX
            + "(" + COLUMN_BOX_ID + " INTEGER PRIMARY KEY"
            + "," + COLUMN_BOX_NAME + " TEXT"
            + "," + COLUMN_BOX_TYPE + " INTEGER"
            + "," + COLUMN_BOX_SEQ + " INTEGER"
            + ")";

    // CARD table create statement
    private static final String CREATE_TABLE_CARD = "CREATE TABLE " + TABLE_CARD
            + "(" + COLUMN_CARD_ID + " INTEGER PRIMARY KEY"
            + "," + COLUMN_CARD_NAME + " TEXT"
            + "," + COLUMN_CARD_IMAGE_PATH + " TEXT"
            + "," + COLUMN_CARD_TYPE + " INTEGER"
            + "," + COLUMN_CARD_SEQ + " INTEGER"
            + "," + COLUMN_CARD_BOX_ID + " INTEGER"
            + ")";

    // table field list
    // BOX table field list
    private static final String FIELD_LIST_BOX = COLUMN_BOX_ID
            + "," + COLUMN_BOX_NAME + "," + COLUMN_BOX_TYPE + "," + COLUMN_BOX_SEQ;

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
        db.execSQL(CREATE_TABLE_BOX);
        db.execSQL(CREATE_TABLE_CARD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOX);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARD);

        // create new tables
        onCreate(db);
    }

    public BoxDTO addBox(String name){
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOX_NAME, name);
        values.put(COLUMN_BOX_TYPE, 0);
        values.put(COLUMN_BOX_SEQ, 0);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_BOX, null, values);
        db.close();

        return getBox(name);
    }

    public BoxDTO getBox(String name) {
        BoxDTO box = null;

        String query = "select " + FIELD_LIST_BOX
                + " from " + TABLE_BOX
                + " where " + COLUMN_BOX_NAME + " = \"" + name + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

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

    public BoxDTO getBox(int id) {
        BoxDTO box = null;

        String query = "select " + FIELD_LIST_BOX
                + " from " + TABLE_BOX
                + " where " + COLUMN_BOX_ID + " = " + id;

        SQLiteDatabase db = this.getWritableDatabase();

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
        values.put(COLUMN_BOX_NAME, box.getName());
        values.put(COLUMN_BOX_TYPE, box.getType());
        values.put(COLUMN_BOX_SEQ, box.getSeq());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_BOX, null, values);
        db.close();

        return box;
    }


}
