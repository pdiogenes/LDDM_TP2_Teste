package com.example.pdiogenes.treerecyclerview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NodeController {
    private SQLiteDatabase db;
    private DBHelper database;
    public NodeController(Context context){
        database = new DBHelper(context);
    }

    public String insertNode(String nodeID, String nodeContent, String parentID){
        ContentValues values;
        long result;
        db = database.getWritableDatabase();
        values = new ContentValues();
        values.put(DBHelper.NODE_ID_COLUMN, nodeID);
        values.put(DBHelper.NODE_CONTENT_COLUMN, nodeContent);
        values.put(DBHelper.NODE_PARENTID_COLUMN, parentID);
        result = db.insert(DBHelper.TREE_TABLE_NAME, null, values);
        db.close();
        if (result ==-1) return "Error while inserting row";
        else return "Row successfully inserted";
    }

    public void updateNode(String nodeID, String nodeContent, String parentID){
        ContentValues values;
        String where;
        db = database.getWritableDatabase();
        where = DBHelper.NODE_ID_COLUMN + "= '" + nodeID + "'";
        values = new ContentValues();
        values.put(DBHelper.NODE_ID_COLUMN, nodeID);
        values.put(DBHelper.NODE_CONTENT_COLUMN, nodeContent);
        values.put(DBHelper.NODE_PARENTID_COLUMN, parentID);
        db.update(DBHelper.TREE_TABLE_NAME, values, where,null);
        db.close();
    }

    public void deleteNode(String nodeID){
        String where = DBHelper.NODE_ID_COLUMN + "= '" + nodeID + "'";
        db = database.getReadableDatabase();
        db.delete(DBHelper.TREE_TABLE_NAME, where,null);
        db.close();
    }

    public Cursor selectAll(){
        Cursor cursor;
        String[] fields = new String[]{database.NODE_ID_COLUMN, database.NODE_CONTENT_COLUMN,
                database.NODE_PARENTID_COLUMN};
        db = database.getReadableDatabase();
        cursor = db.query(database.TREE_TABLE_NAME, fields, null, null, null, null,
                database.NODE_ID_COLUMN +" ASC", null);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }


}
