package com.example.pdiogenes.treerecyclerview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerInterface, leafContent.OnFragmentInteractionListener {

    public RecyclerView rvNodes;
    LinearLayoutManager llm;
    NodeAdapter adapter;
    private Node raiz = new Node("root", "root", null); //adding a root node
    //private List<Node> nodeList = new ArrayList<Node>();
    private List<Node> nodeList;
    public ImageButton btnAddExterno;
    public ImageButton btnReturn;
    public String nodeContent = "";
    public Node currentParent;
    public Button btnDB;

    DBHelper helper;
    NodeController controller;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nodeList = raiz.getChildren(); //starting the nodelist as the root child list

        /* Setting up the Recycler View */
        rvNodes = findViewById(R.id.rvNodes);
        llm = new LinearLayoutManager(this);
        rvNodes.setLayoutManager(llm);
        adapter = new NodeAdapter(this, nodeList, this);
        rvNodes.setAdapter(adapter);

        /* Setting up the node insertion button */
        btnAddExterno = findViewById(R.id.btnAddExterno);
        btnAddExterno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nodeInsertionPrep();
            }
        });

        // setting up the return button
        btnReturn = findViewById(R.id.btnParent);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToParent();
            }
        });

        //setting up the DB button
        btnDB = findViewById(R.id.btnDB);
        btnDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DBActivity.class));
            }
        });

        helper = new DBHelper(this);
        helper.getWritableDatabase();
        controller = new NodeController(this);

    }

    /*
    setting up the external node addition
    fetches the current size of the node list
    uses "currentParent" as a dummy node in case of empty list (due to deletion)
    otherwise directly fetches the parent from a random node of the current list
      */
    public void nodeInsertionPrep(){
        int currentNodes = nodeList.size();
        String nodeID;
        if(nodeList == raiz.getChildren()){
            nodeID = (nodeList.size() == 0)
                    ? "1" : Integer.toString(Integer.parseInt(nodeList.get(nodeList.size() - 1).getIdNode()) + 1);;

        }
        else if(nodeList != raiz.getChildren() && nodeList.size() == 0){
            String parentId = currentParent.getIdNode();
            nodeID = parentId + "." + (currentNodes+1);
        }
        else{
            String parentId = nodeList.get(0).getParent().getIdNode();
            nodeID = parentId + "." + (currentNodes+1);
        }
        throwContentDialog(nodeID);
    }

    /*
    inserts the node after the prep
    receives the new nodes ID and its content
    uses "currentParent" as a dummy node in case of an empty list (excluding the root, due to deletion)
    otherwise fetches the parent directly from the current node list
    inserts the new node with its info and its parent, and notifies the user
     */
    public void addNode(String nodeID, String content){
        Node newNode;
        if(nodeList == raiz.getChildren()){
            newNode = new Node(nodeID, content, raiz);
            nodeList.add(newNode);
            adapter.notifyDataSetChanged();
        }
        else if(nodeList.size() == 0){
            newNode = new Node(nodeID, content, currentParent);
            nodeList.add(newNode);
            adapter.notifyDataSetChanged();
        }
        else{
            Node parent = nodeList.get(0).getParent();
            newNode = new Node(nodeID, content, parent);
            nodeList.add(newNode);
            adapter.notifyDataSetChanged();
        }
        String result = "";
        result = controller.insertNode(newNode.getIdNode(), newNode.getContent(), newNode.getParent().getIdNode());
        Toast.makeText(this, "Node added", Toast.LENGTH_SHORT).show();
    }

    /*
    return to parent function
    fetches a random node from the current list, or the parent of the current node using a dummy node
    fetches the parent of the parent of the random node, or the parent of the dummy node in order to fetch its child list
    updates the recycler view to show the parent nodes list
     */
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
                currentParent = nodeList.get(0).getParent();
            }
        }
        else if(nodeList != raiz.getChildren() && nodeList.size() == 0){
            nodeList = currentParent.getParent().getChildren();
            adapter = new NodeAdapter(this, nodeList, this);
            //rvNodes.swapAdapter(adapter, true);
            rvNodes.setAdapter(adapter);
            currentParent = nodeList.get(0).getParent();
        }
        else{
            Toast.makeText(this, "There are no nodes", Toast.LENGTH_SHORT).show();
        }

    }

    /* setting up the content alert dialog
    receives the new node ID and provides the nodes content to the node insertion method
    */
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
                    addNode(NodeID, "No content");
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

    /*
    starts a new fragment to show the content of leaf nodes
     */
    public void showFragment(String nodeID, String nodeContent){
        leafContent leaf = leafContent.newInstance(nodeID, nodeContent);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right,
                R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.addToBackStack(null);
        transaction.add(R.id.fragmentContainer, leaf, "LEAF").commit();
    }

    /*
    opens the child node list, or a fragment with the leaf nodes content
     */
    @Override
    public void onItemClick(Object object){
        Node clickedNode = (Node) object;
        if(clickedNode.isLeaf()){
            String id = clickedNode.getIdNode();
            String content = clickedNode.getContent();
            showFragment(id, content);
        }
        else{
            currentParent = clickedNode;
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
