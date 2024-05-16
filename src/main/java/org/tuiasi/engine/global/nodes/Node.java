package org.tuiasi.engine.global.nodes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tuiasi.engine.global.nodes.reflexive.ReflexiveObjectManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data @NoArgsConstructor @AllArgsConstructor
public class Node<T> {
    private Integer salt;

    @EditorVisible
    private String name;
    private ArrayList<Node<?>> children;
    private Node<?> parent;

    private T value;
    private ReflexiveObjectManager rom;


    public Node(Node<?> parent, String name){
        this.salt = new Random().nextInt();

        this.parent = parent;
        this.name = name;
        this.value = null;

        if(parent != null){
            parent.addChild(this);
        }

        this.children = new ArrayList<>();
    }

    public Node(String name){
        this(null, name);
    }

    public Node(Node<?> parent, String name, T value){
        this(parent, name);
        this.value = value;
        this.rom = new ReflexiveObjectManager(value);
    }

    // override hash function
    @Override
    public int hashCode() {
        // create a hashcode using the name and the children
        return salt.hashCode() + name.hashCode();
    }

    public void addChild(Node<?> child){
        children.add(child);
    }

    public void removeChild(Node<?> child){
        children.remove(child);
    }

    public Node getChildByName(String name){
        for(Node<?> child : children){
            if(child.getName().equals(name)){
                return child;
            }
        }
        return null;
    }

    public void printTree(){
        printTree(0);
    }

    private void printTree(int level){
        for(int i = 0; i < level; i++){
            System.out.print("  ");
        }
        System.out.println(value);

        for(Node<?> child : children){
            child.printTree(level + 1);
        }
    }

    public List<String> getFields(){
        return rom.getFields();
    }

    public void setFieldValue(String fieldName, Object value) {
        rom.setValue(fieldName, value);
    }

    public Object getFieldValue(String fieldName) {
        return rom.getValue(fieldName);
    }

    public Node<?> findNode(NodeSearchFunction function){
        if(function.search(this)){
            return this;
        }

        for(Node<?> child : children){
            Node<?> found = child.findNode(function);
            if(found != null){
                return found;
            }
        }

        return null;
    }
}
