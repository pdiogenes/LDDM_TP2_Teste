package com.example.pdiogenes.treerecyclerview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.Image;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerInterface, leafContent.OnFragmentInteractionListener {

    public RecyclerView rvNodes;
    LinearLayoutManager llm;
    NodeAdapter adapter;
    private Node raiz = new Node("raiz", "raiz", null);
    //private List<Node> nodeList = new ArrayList<Node>();
    private List<Node> nodeList;
    public ImageButton btnAddExterno;
    public ImageButton btnReturn;
    public String nodeContent = "";
    public Node paiAtual;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nodeList = raiz.getChildren();

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
                nodeInsertionPrep();
            }
        });

        btnReturn = findViewById(R.id.btnParent);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToParent();
            }
        });

    }

    /* setting up the external node addition */
    public void nodeInsertionPrep(){
        int currentNodes = nodeList.size();
        String nodeID;
        if(nodeList == raiz.getChildren()){
            nodeID = Integer.toString(currentNodes + 1);
        }
        else if(nodeList != raiz.getChildren() && nodeList.size() == 0){
            String parentId = paiAtual.getIdNode();
            nodeID = parentId + "." + (currentNodes+1);
        }
        else{
            String parentId = nodeList.get(0).getParent().getIdNode();
            nodeID = parentId + "." + (currentNodes+1);
        }
        throwContentDialog(nodeID);
    }

    public void addNode(String nodeID, String content){
        Node newNode;
        if(nodeList == raiz.getChildren()){
            newNode = new Node(nodeID, content, raiz);
            nodeList.add(newNode);
            adapter.notifyDataSetChanged();
        }
        else if(nodeList.size() == 0){
            newNode = new Node(nodeID, content, paiAtual);
            nodeList.add(newNode);
            adapter.notifyDataSetChanged();
        }
        else{
            Node parent = nodeList.get(0).getParent();
            newNode = new Node(nodeID, content, parent);
            nodeList.add(newNode);
            adapter.notifyDataSetChanged();
        }
        Toast.makeText(this, "Node added", Toast.LENGTH_SHORT).show();
    }

    public void returnToParent(){
        if(!nodeList.isEmpty()){
            Node atualQualquer = nodeList.get(0);
            Node parentOfParent = atualQualquer.getParent().getParent();

            if(parentOfParent == null || nodeList.isEmpty()){
                Toast.makeText(this, "Already at root", Toast.LENGTH_SHORT).show();
            } else {
                nodeList = parentOfParent.getChildren();
                adapter = new NodeAdapter(this, nodeList, this);
                //rvNodes.swapAdapter(adapter, true);
                rvNodes.setAdapter(adapter);
            }
        }
        else if(nodeList != raiz.getChildren() && nodeList.size() == 0){
            nodeList = paiAtual.getParent().getChildren();
            adapter = new NodeAdapter(this, nodeList, this);
            //rvNodes.swapAdapter(adapter, true);
            rvNodes.setAdapter(adapter);
        }
        else{
            Toast.makeText(this, "There are no nodes", Toast.LENGTH_SHORT).show();
        }

    }

    /* setting up the content alert dialog */
    public void throwContentDialog(final String NodeID){
        AlertDialog.Builder contentDialog = new AlertDialog.Builder(this);
        final EditText txtContent = new EditText(this);
        txtContent.setHint("Enter the node content");
        contentDialog.setTitle("Adding new node");
        contentDialog.setCancelable(true);

        contentDialog.setView(txtContent);
        contentDialog.setPositiveButton("Add node", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                nodeContent = txtContent.getText().toString();
                if(nodeContent.equals("")){
                    addNode(NodeID, "Não há conteudo");
                }
                else addNode(NodeID, nodeContent);
            }
        });

        contentDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        contentDialog.show();
    }

    public void showFragment(String nodeID, String nodeContent){
        leafContent leaf = leafContent.newInstance(nodeID, nodeContent);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right,
                R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.addToBackStack(null);
        transaction.add(R.id.fragmentContainer, leaf, "LEAF").commit();
    }


    @Override
    public void onItemClick(Object object){
        Node clickedNode = (Node) object;
        if(clickedNode.isLeaf()){
            String id = clickedNode.getIdNode();
            String content = clickedNode.getContent();
            showFragment(id, content);
        }
        else{
            paiAtual = clickedNode;
            nodeList = clickedNode.getChildren();
            adapter = new NodeAdapter(this, nodeList, this);
            //rvNodes.swapAdapter(adapter, true);
            rvNodes.setAdapter(adapter);
        }
    }

    @Override
    public void onFragmentInteraction() {
        onBackPressed();
    }
}
