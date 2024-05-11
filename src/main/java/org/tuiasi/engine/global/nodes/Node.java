package org.tuiasi.engine.global.nodes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Random;

@Data @NoArgsConstructor @AllArgsConstructor
public class Node {
    Integer salt;

    String name;
    ArrayList<Node> children;
    Node parent;

    public Node(Node parent, String name){
        this.salt = new Random().nextInt();

        this.parent = parent;
        this.name = name;

        if(parent != null){
            parent.addChild(this);
        }

        this.children = new ArrayList<>();
    }

    // override hash function
    @Override
    public int hashCode() {
        // create a hashcode using the name and the children
        return salt.hashCode() + name.hashCode();
    }

    public Node(String name){
        this(null, name);

        this.children = new ArrayList<>();
    }

    public Object getValue(){
        return null;
    }

    public void addChild(Node child){
        children.add(child);
    }

    public void removeChild(Node child){
        children.remove(child);
    }

    public void printTree(){
        printTree(0);
    }

    private void printTree(int level){
        for(int i = 0; i < level; i++){
            System.out.print("  ");
        }
        System.out.println(name);

        for(Node child : children){
            child.printTree(level + 1);
        }
    }

}
