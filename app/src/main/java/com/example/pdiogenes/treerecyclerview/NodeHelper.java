package com.example.pdiogenes.treerecyclerview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pdiogenes.treerecyclerview.DB.NodeController;

import java.util.List;

public class NodeHelper extends MainActivity{




    public Context ctx;
    public String conteudo = "";
    NodeAdapter adapter;

    NodeController controller;

    NodeHelper(Context ctx){
        this.ctx = ctx;
    }

    public String showInsertDialog(final Node selectedNode, final String nodeID){
        AlertDialog.Builder contentDialog = new AlertDialog.Builder(ctx);
        final EditText txtContent = new EditText(ctx);
        txtContent.setHint("Enter the node content");
        contentDialog.setTitle("Adding new node");
        contentDialog.setCancelable(true);

        contentDialog.setView(txtContent);
        contentDialog.setPositiveButton("Add node", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                conteudo = txtContent.getText().toString();
                if(conteudo.equals("")){
                    conteudo = "Não há conteudo";
                }
                insertNode(selectedNode, nodeID, conteudo);
            }
        });

        contentDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
                showToastDenied();
            }
        });

        contentDialog.show();

        return conteudo;
    }

    public void showUpdateDialog(final Node selectedNode){
        AlertDialog.Builder contentDialog = new AlertDialog.Builder(ctx);
        final EditText txtContent = new EditText(ctx);
        txtContent.setText(selectedNode.getContent());
        txtContent.requestFocus();
        contentDialog.setTitle("Update the node's content");
        contentDialog.setCancelable(true);

        contentDialog.setView(txtContent);
        contentDialog.setPositiveButton("Update node", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                conteudo = txtContent.getText().toString();
                if(conteudo.equals("")){
                    conteudo = "Não há conteudo";
                }
                updateNode(selectedNode, conteudo);
            }
        });

        contentDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
                showToastDenied();
            }
        });

        contentDialog.show();
    }


    public void insertNode(Node selectedNode, String nodeID, String nodeContent){
        controller = new NodeController(ctx);
        Node n = new Node(nodeID, nodeContent);
        selectedNode.addChild(n);
        String result = "";
        result = controller.insertNode(n.getIdNode(), n.getContent(), n.getParent().getIdNode());
        showToastConfirmed();
    }

    public void updateNode(Node selectedNode, String conteudo){
        controller = new NodeController(ctx);
        selectedNode.setContent(conteudo);
        controller.updateNode(selectedNode.getIdNode(), selectedNode.getContent(), selectedNode.getParent().getIdNode());
        showToastUpdated();
    }

    public void showToastUpdated(){
        Toast.makeText(ctx, "Node updated", Toast.LENGTH_SHORT).show();
    }

    public void showToastConfirmed(){
        Toast.makeText(ctx, "Node inserted", Toast.LENGTH_SHORT).show();
    }

    public void showToastDenied(){
        Toast.makeText(ctx, "Action canceled", Toast.LENGTH_SHORT).show();
    }

}
