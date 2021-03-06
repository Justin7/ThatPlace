package com.my.thatplace;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
   
   
public class PlaceDbAdapter {
   
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "data";
	private static final String DATABASE_TABLE = "table_place";
	public static final String KEY_ROWID = "_id";
    public static final String KEY_STN = "stn";
    public static final String KEY_GATE = "gate";
	private static final String KEY_PLACE = "place";
    private static final String TAG = "ThatPlaceDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static final String DATABASE_CREATE =
    	"CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        KEY_STN + " TEXT, " +
        KEY_GATE + " TEXT, " +
        KEY_PLACE + " TEXT);";

    private final Context mCtx;
   
    private static class DatabaseHelper extends SQLiteOpenHelper {
   
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
   
        @Override
        public void onCreate(SQLiteDatabase db) {
   
            db.execSQL(DATABASE_CREATE);
        }
   
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_CREATE);
            onCreate(db);
        }
    }
   
    public PlaceDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }
   
    public PlaceDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
   
    public void close() {
        mDbHelper.close();
    }
    
   
    public long createPlace(String stn, String gate, String place) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_STN, stn);
        initialValues.put(KEY_GATE, gate);
        initialValues.put(KEY_PLACE, place);
   
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }
   
    public boolean deletePlace(long rowId) {
   
        Log.i("Delete called", "value__" + rowId);
        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
   
    public Cursor fetchAllPlaces() {
   
        return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_STN,
                KEY_GATE, KEY_PLACE }, null, null, null, null, null);
    }
   
    public Cursor fetchPlaceByStn(String stn) throws SQLException {
        Cursor mCursor = mDb.query(true, DATABASE_TABLE,
        		new String[] { KEY_ROWID, KEY_STN,	KEY_GATE, KEY_PLACE },
        		KEY_STN + "=" + "'" + stn + "'", null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor fetchPlaceByPlace(String place) throws SQLException {
    	
    	Cursor mCursor = mDb.query(true, DATABASE_TABLE,
    			new String[] { KEY_ROWID, KEY_STN,	KEY_GATE, KEY_PLACE },
    			 KEY_PLACE + "=" + "'" + place + "'", null, null, null, null,null);
    	if (mCursor != null) {
    		mCursor.moveToFirst();
    	}
    	return mCursor;
    }
    
    public Cursor fetchPlaceByStnAndPlace(String stn, String place) throws SQLException {
    	
    	Cursor mCursor = mDb.query(true, DATABASE_TABLE,
    			new String[] { KEY_ROWID, KEY_STN,	KEY_GATE, KEY_PLACE },
    			KEY_STN + "=" + "'" + stn +  "'" +  " and " + KEY_PLACE + "=" + "'" + place + "'", null, null, null, null,null);
    	if (mCursor != null) {
    		mCursor.moveToFirst();
    	}
    	return mCursor;
    }
   
    public boolean updatePlace(long rowId, String stn, String gate, String place) {
        ContentValues args = new ContentValues();
        args.put(KEY_STN, stn);
        args.put(KEY_GATE, gate);
        args.put(KEY_PLACE, place);
   
        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}