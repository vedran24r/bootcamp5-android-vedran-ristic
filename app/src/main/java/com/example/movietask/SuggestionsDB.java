package com.example.movietask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;

public class SuggestionsDB {
    public  static final String KEY_ROWID = "_id";
    public  static final String KEY_SUGGESTION = "suggestion";

    public  final String DATABASE_NAME = "SuggestionsDB";
    public  final String DATABASE_TABLE = "SuggestionsTable";
    public  final int DATABASE_VERSION = 1;

    private  DBHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;

    public SuggestionsDB (Context context){
        ourContext = context;
    }

    private class DBHelper extends SQLiteOpenHelper{
        public DBHelper (Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sqlCode = "CREATE TABLE " + DATABASE_TABLE + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_SUGGESTION + " TEXT NOT NULL) ";
            db.execSQL(sqlCode);
        }
    }
    public SuggestionsDB open() throws SQLException{
        ourHelper = new DBHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        ourHelper.close();
    }

    public long createEntry(String suggestion){
        ContentValues cv = new ContentValues();
        cv.put(KEY_SUGGESTION, suggestion);
        return ourDatabase.insert(DATABASE_TABLE, null, cv);
    }
    public ArrayList<String> getSuggestions(){
        String [] columns = new String[]{KEY_SUGGESTION};
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
        String result = "";
        ArrayList<String> suggestions = new ArrayList<>();

        int iSuggestion = c.getColumnIndex(KEY_SUGGESTION);

        for(c.moveToFirst() ; !c.isAfterLast() ; c.moveToNext()){

            suggestions.add(c.getString(iSuggestion));
            result += c.getString(iSuggestion)+"\n";
        }
        Collections.reverse(suggestions);
        c.close();
        return suggestions;
    }
    public long deleteEntry(String rowId){
        return ourDatabase.delete(DATABASE_TABLE, KEY_ROWID + "=?", new String[]{rowId});
    }
    public long updateEntry(String rowId, String suggestion){
        ContentValues cv = new ContentValues();
        cv.put(KEY_SUGGESTION, suggestion);
        return ourDatabase.update(DATABASE_TABLE, cv, KEY_ROWID + "=?", new String[]{rowId});
    }
}

