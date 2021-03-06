package com.example.pdiogenes.treerecyclerview;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class NodeAdapter extends RecyclerView.Adapter<NodeAdapter.NodeViewHolder> {
    public static RecyclerInterface recyclerInterface;
    static Context context;
    private List<Node> nodeList;

    public NodeAdapter(Context ctx, List<Node> list, RecyclerInterface clickRecyclerInterface){
        this.context = ctx;
        this.nodeList = list;
        this.recyclerInterface = clickRecyclerInterface;
    }

    @Override
    public void onBindViewHolder(NodeViewHolder holder, final int i){
        Node no = nodeList.get(i);
        holder.txtNodeName.setText(no.getIdNode());
        holder.btnAdd.setOnClickListener(new View.OnClickListener(){
            public void onClick (View V){
                Context ctx = NodeAdapter.context;
                NodeHelper helper = new NodeHelper(ctx);
                Node selectedNode = nodeList.get(i);
                int currentNodes = selectedNode.getChildren().size();
                String childNodeID = selectedNode.getIdNode() + "." + (currentNodes+1);
                String childNodeContent = helper.showInsertDialog(selectedNode, childNodeID);
            }
        });
        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context ctx = NodeAdapter.context;
                NodeHelper helper = new NodeHelper(ctx);
                Node selectedNode = nodeList.get(i);
                helper.showUpdateDialog(selectedNode);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context ctx = NodeAdapter.context;
                NodeController controller = new NodeController(ctx);
                Node selectedNode = nodeList.get(i);
                controller.deleteNode(selectedNode.getIdNode());
                nodeList.remove(selectedNode);
                notifyItemRemoved(i);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public NodeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.node_item,
                viewGroup, false);
        return new NodeViewHolder(itemView);
    }

    @Override
    public int getItemCount(){
        return nodeList.size();
    }

    protected class NodeViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtNodeName;
        protected ImageButton btnAdd;
        protected ImageButton btnUpdate;
        protected ImageButton btnDelete;

        public NodeViewHolder(final View itemView){
            super(itemView);
            txtNodeName = (TextView) itemView.findViewById(R.id.txtNodeName);
            btnAdd = (ImageButton) itemView.findViewById(R.id.btnNodeAdd);
            btnUpdate = (ImageButton) itemView.findViewById(R.id.btnNodeUpdate);
            btnDelete = (ImageButton) itemView.findViewById(R.id.btnNodeDelete);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    recyclerInterface.onItemClick(nodeList.get(getLayoutPosition()));
                }
            });
        }
    }
}
