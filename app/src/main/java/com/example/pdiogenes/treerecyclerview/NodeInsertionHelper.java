package com.example.pdiogenes.treerecyclerview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;

public class NodeInsertionHelper extends MainActivity{

    public Context ctx;
    public String conteudo = "";

    NodeInsertionHelper(Context ctx){
        this.ctx = ctx;
    }

    public String showDialog(final Node selectedNode, final String nodeID){
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

    public void insertNode(Node selectedNode, String nodeID, String nodeContent){
        Node n = new Node(nodeID, nodeContent);
        selectedNode.addChild(n);
        showToastConfirmed();
    }

    public void showToastConfirmed(){
        Toast.makeText(ctx, "Node inserted", Toast.LENGTH_SHORT).show();
    }

    public void showToastDenied(){
        Toast.makeText(ctx, "Node not inserted", Toast.LENGTH_SHORT).show();
    }

}
