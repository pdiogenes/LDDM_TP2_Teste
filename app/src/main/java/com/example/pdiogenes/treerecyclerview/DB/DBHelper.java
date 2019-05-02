package com.example.pdiogenes.treerecyclerview.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "TreeDB.db";
    public static final int DB_VERSION = 1;
    public static final String TREE_TABLE_NAME = "Nodes";
    public static final String NODE_ID_COLUMN = "_id";
    public static final String NODE_CONTENT_COLUMN = "NodeContent";
    public static final String NODE_PARENTID_COLUMN = "ParentID";

    public DBHelper (Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NODE_TABLE = "CREATE TABLE "+
                TREE_TABLE_NAME + "(" +
                NODE_ID_COLUMN + " TEXT PRIMARY KEY, " +
                NODE_CONTENT_COLUMN + " TEXT, " +
                NODE_PARENTID_COLUMN + " TEXT)";
        db.execSQL(CREATE_NODE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TREE_TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + TREE_TABLE_NAME);
        onCreate(db);
    }
}
