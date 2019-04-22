package com.example.pdiogenes.treerecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerInterface {

    public RecyclerView rvNodes;
    LinearLayoutManager llm;
    NodeAdapter adapter;
    private List<Node> nodeList = new ArrayList<Node>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvNodes = findViewById(R.id.rvNodes);
        llm = new LinearLayoutManager(this);
        rvNodes.setLayoutManager(llm);
        adapter = new NodeAdapter(this, nodeList, this);
        rvNodes.setAdapter(adapter);
    }



    @Override
    public void onItemClick(Object object){}
}
