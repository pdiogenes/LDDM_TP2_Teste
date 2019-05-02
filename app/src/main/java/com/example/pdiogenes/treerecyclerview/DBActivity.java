package com.example.pdiogenes.treerecyclerview;

import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pdiogenes.treerecyclerview.DB.DBHelper;
import com.example.pdiogenes.treerecyclerview.DB.NodeController;
import com.example.pdiogenes.treerecyclerview.Node;
import com.example.pdiogenes.treerecyclerview.R;

import java.util.List;

public class DBActivity extends AppCompatActivity {

    static Spinner nodes;
    static Cursor cursor;
    static String[] fieldNames;
    static int[] idViews;
    NodeController controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        nodes = (Spinner) findViewById(R.id.spinner);
        controller = new NodeController(this);

        fillAdapter();
    }

    public void fillAdapter(){
        cursor = controller.selectAll();
        fieldNames = new String[]{DBHelper.NODE_ID_COLUMN, DBHelper.NODE_CONTENT_COLUMN,
                DBHelper.NODE_PARENTID_COLUMN};
        idViews = new int[]{R.id.txtIDNode, R.id.txtContentNode,
                R.id.txtNodeParent};
        SimpleCursorAdapter adaptador = new SimpleCursorAdapter(this,
                R.layout.spinner_layout, cursor, fieldNames, idViews, 0);
        nodes.setAdapter(adaptador);
    }
}
