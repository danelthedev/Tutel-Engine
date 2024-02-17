package org.tuiasi.engine.ui.composedComponents;

import imgui.ImGui;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tuiasi.engine.ui.basicComponents.searchbar.ISearchbar;
import org.tuiasi.engine.ui.basicComponents.searchbar.SearchListener;
import org.tuiasi.engine.ui.basicComponents.searchbar.SearchbarWithHint;
import org.tuiasi.engine.ui.basicComponents.tree.ITree;
import org.tuiasi.engine.ui.basicComponents.tree.Tree;
import org.tuiasi.engine.ui.basicComponents.tree.TreeListener;
import org.tuiasi.engine.ui.basicComponents.tree.TreeNode;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TreeWithTitleAndSearchBar {

    private String title;
    private SearchbarWithHint searchbar;
    private Tree tree;
    private List<TreeNode> originalTreeData;
    private List<TreeNode> filteredTreeData;

    public TreeWithTitleAndSearchBar(String hint, String title, List<TreeNode> treeData, TreeListener nodeClickListener, SearchListener searchListener) {
        this.title = title;

        this.searchbar = new SearchbarWithHint(hint);
        this.searchbar.setSearchListener(searchListener);

        this.tree = new Tree(treeData, nodeClickListener);
        this.originalTreeData = new ArrayList<>(treeData);
        this.filteredTreeData = new ArrayList<>(treeData);
    }

    public void render() {
        ImGui.text(title);

        searchbar.render();
        ImGui.newLine();
        ImGui.separator();

        // Filter tree data based on the search text
        if (searchbar.isEnterPressed()) {
            filterTreeData();
            searchbar.setEnterPressed(false); // Reset the flag
        }

        // Draw the filtered tree
        tree.setTreeData(filteredTreeData);
        tree.render();
    }

    private void filterNodes(List<TreeNode> nodes, String searchTerm) {
        for (TreeNode node : nodes) {
            String nodeName = node.getName();

            // Check if the node name contains the search term
            if (nodeName.toLowerCase().contains(searchTerm)) {
                filteredTreeData.add(node);
            } else {
                filterNodes(node.getChildren(), searchTerm);
            }
        }
    }

    private void filterTreeData() {
        String searchTerm = searchbar.getSearchText().get().toLowerCase();
        if (searchTerm.isEmpty()) {
            filteredTreeData = new ArrayList<>(originalTreeData); // Reset to the original tree data
        } else {
            filteredTreeData = new ArrayList<>();
            filterNodes(originalTreeData, searchTerm);
        }
    }

    public void setTreeData(List<TreeNode> treeData) {
        originalTreeData = new ArrayList<>(treeData);
        filteredTreeData = new ArrayList<>(treeData);
        tree.setTreeData(treeData);
    }

    public void setNodeClickListener(TreeListener listener) {
        tree.setNodeClickListener(listener);
    }

}