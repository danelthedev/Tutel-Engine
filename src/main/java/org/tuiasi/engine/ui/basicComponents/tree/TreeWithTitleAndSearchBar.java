package org.tuiasi.engine.ui.basicComponents.tree;

import imgui.ImGui;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tuiasi.engine.ui.basicComponents.searchbar.SearchbarWithHint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class TreeWithTitleAndSearchBar extends SearchbarWithHint implements ITree {

    private String title;
    private Tree treeWithTitle;
    private List<TreeNode> originalTreeData;
    private List<TreeNode> filteredTreeData;

    public TreeWithTitleAndSearchBar(String hint, String title, List<TreeNode> treeData, NodeClickListener nodeClickListener) {
        super(hint);
        this.title = title;
        this.treeWithTitle = new Tree(treeData, nodeClickListener);
        this.originalTreeData = new ArrayList<>(treeData);
        this.filteredTreeData = new ArrayList<>(treeData);
    }


    public TreeWithTitleAndSearchBar(String hint, String title, List<TreeNode> treeData, NodeClickListener nodeClickListener, SearchListener searchListener) {
        super(hint);
        this.title = title;
        this.treeWithTitle = new Tree(treeData, nodeClickListener);
        this.originalTreeData = new ArrayList<>(treeData);
        this.filteredTreeData = new ArrayList<>(treeData);
        setSearchListener(searchListener);
    }

    @Override
    public void render() {
        ImGui.text(title);
        super.render(); // Render the search bar
        ImGui.newLine();
        ImGui.separator();

        // Filter tree data based on the search text
        if (isEnterPressed()) {
            filterTreeData();
            setEnterPressed(false); // Reset the flag
        }

        // Draw the filtered tree
        treeWithTitle.setTreeData(filteredTreeData);
        treeWithTitle.render();
    }

    private void filterNodes(List<TreeNode> nodes, String searchTerm) {
        for (TreeNode node : nodes) {
            String nodeName = node.getName();

            // Check if the node name contains the search term
            if (nodeName.toLowerCase().contains(searchTerm)) {
                filteredTreeData.add(node);
            }
            else{
                filterNodes(node.getChildren(), searchTerm);
            }
        }

    }

    private void filterTreeData() {
        String searchTerm = getSearchText().get().toLowerCase();
        if (searchTerm.isEmpty()) {
            filteredTreeData = new ArrayList<>(originalTreeData); // Reset to the original tree data
        } else {
            filteredTreeData = new ArrayList<>();
            filterNodes(originalTreeData, searchTerm);
        }
    }

    @Override
    public void setTreeData(List<TreeNode> treeData) {
        originalTreeData = new ArrayList<>(treeData);
        filteredTreeData = new ArrayList<>(treeData);
        treeWithTitle.setTreeData(treeData);
    }

    @Override
    public void setNodeClickListener(NodeClickListener listener) {
        treeWithTitle.setNodeClickListener(listener);
    }
}