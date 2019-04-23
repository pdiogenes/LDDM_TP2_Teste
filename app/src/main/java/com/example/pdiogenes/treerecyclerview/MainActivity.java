package com.example.pdiogenes.treerecyclerview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerInterface {

    public RecyclerView rvNodes;
    LinearLayoutManager llm;
    NodeAdapter adapter;
    private List<Node> nodeList = new ArrayList<Node>();
    public ImageButton btnAddExterno;
    public String nodeContent = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Setting up the Recycler View */
        rvNodes = findViewById(R.id.rvNodes);
        llm = new LinearLayoutManager(this);
        rvNodes.setLayoutManager(llm);
        adapter = new NodeAdapter(this, nodeList, this);
        rvNodes.setAdapter(adapter);

        /* Setting up the external node add button */
        btnAddExterno = findViewById(R.id.btnAddExterno);
        btnAddExterno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                externalNodePrep();
            }
        });

    }

    /* setting up the external node addition */
    public void externalNodePrep(){
        int currentNodes = nodeList.size();
        String externalNodeID = Integer.toString(currentNodes + 1);
        throwContentDialog(externalNodeID);
    }

    public void addExternalNode(String externalNodeID, String content){
        Node externalNode = new Node(externalNodeID, content, null);
        nodeList.add(externalNode);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Node added", Toast.LENGTH_SHORT).show();
    }

    /* setting up the content alert dialog */
    public void throwContentDialog(final String externalNodeID){
        AlertDialog.Builder contentDialog = new AlertDialog.Builder(this);
        final EditText txtContent = new EditText(this);
        txtContent.setHint("Enter the node content");
        contentDialog.setTitle("Adding new node");
        contentDialog.setCancelable(true);

        contentDialog.setView(txtContent);
        contentDialog.setPositiveButton("Add node", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                nodeContent = txtContent.getText().toString();
                addExternalNode(externalNodeID, nodeContent);
            }
        });

        contentDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        contentDialog.show();
    }


    @Override
    public void onItemClick(Object object){
        Node clickedNode = (Node) object;
        if(clickedNode.isLeaf()){
            // add fragment code
        }
        else{
            List<Node> newNodeList = clickedNode.getChildren();
            NodeAdapter nAdapter = new NodeAdapter(this, newNodeList, this);
            rvNodes.swapAdapter(nAdapter, false);
            Toast.makeText(this, "xd", Toast.LENGTH_SHORT).show();
        }
    }
}
