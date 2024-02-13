package org.tuiasi.engine.ui.basicComponents.tree;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter @Getter @AllArgsConstructor  @NoArgsConstructor
public class TreeNode {
    private String name;
    private List<TreeNode> children;
    private TreeNode parent;

    public TreeNode(String name) {
        this.name = name;
        this.children = List.of();
        this.parent = null;
    }

    public TreeNode(String name, List<TreeNode> children) {
        this.name = name;
        this.children = children;
        this.parent = null;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
        for(TreeNode child : children) {
            child.setParent(this);
        }
    }
}
