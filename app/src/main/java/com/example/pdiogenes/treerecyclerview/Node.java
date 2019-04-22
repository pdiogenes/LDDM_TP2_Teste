package com.example.pdiogenes.treerecyclerview;

import java.util.ArrayList;
import java.util.List;

public class Node {
    public List<Node> children = new ArrayList<Node>();
    public String idNode;
    public String content;
    public Node parent = null;

    public String getIdNode() {
        return idNode;
    }

    public void setIdNode(String idNode) {
        this.idNode = idNode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        parent.addChild(this);
        this.parent = parent;
    }

    public void addChild(Node child){
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(String content){
        Node child = new Node(content);
        child.setParent(this);
        this.children.add(child);
    }

    public Node(String content) { this.content = content; }

    public Node(String idNode, String content) {
        this.idNode = idNode;
        this.content = content;
    }

    public Node(String content, Node parent){
        this.content = content;
        this.parent = parent;
    }

    public Node(String idNode, String content, Node parent){
        this.idNode = idNode;
        this.content = content;
        this.parent = parent;
    }

    public List<Node> getChildren(){
        return this.children;
    }


}
