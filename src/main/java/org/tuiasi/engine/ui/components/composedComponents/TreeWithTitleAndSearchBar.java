package org.tuiasi.engine.ui.components.composedComponents;

import imgui.ImGui;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tuiasi.engine.global.nodes.Node;
import org.tuiasi.engine.logic.AppLogic;
import org.tuiasi.engine.ui.components.IComponent;
import org.tuiasi.engine.ui.components.basicComponents.searchbar.SearchListener;
import org.tuiasi.engine.ui.components.basicComponents.searchbar.SearchbarWithHint;
import org.tuiasi.engine.ui.components.basicComponents.tree.Tree;
import org.tuiasi.engine.ui.components.basicComponents.tree.TreeListener;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TreeWithTitleAndSearchBar extends IComponent {

    private String label;
    private SearchbarWithHint searchbar;
    private Tree tree;

    private List<List<Node<?>>> matchingPaths;
    private List<Node<?>> matchingNodes;

    public TreeWithTitleAndSearchBar(String hint, String label, Node<?> root, TreeListener nodeClickListener, SearchListener searchListener) {
        this.label = label;

        this.searchbar = new SearchbarWithHint(hint);
        this.searchbar.setSearchListener(searchListener);

        this.tree = new Tree(root, nodeClickListener);

        this.matchingPaths = new ArrayList<>();
        this.matchingNodes = new ArrayList<>();
    }

    public void render() {
        searchbar.render();
        ImGui.separator();

        // Filter tree data based on the search text
        if (searchbar.isEnterPressed() && tree.getRoot() != null) {
            System.out.println("Filtering tree");
            matchingPaths = new ArrayList<>();
            getFilteredPaths(tree.getRoot(), searchbar.getSearchText().get(), new ArrayList<>());
            getFilteredNodes();

            searchbar.setEnterPressed(false); // Reset the flag
        }

        // Set the matching paths to the tree


        tree.render(matchingNodes);
    }

    private void getFilteredPaths(Node<?> node, String searchText, List<Node<?>> path) {
        if(searchText.isEmpty()){
            matchingPaths = new ArrayList<>();
            return;
        }

        path.add(node);

        if (node.getName().toLowerCase().contains(searchText.toLowerCase())) {
            matchingPaths.add(new ArrayList<>(path));
        }

        for (Node<?> child : node.getChildren()) {
            getFilteredPaths(child, searchText, path);
        }

        path.remove(path.size() - 1);
    }

    private void getFilteredNodes() {
        if(matchingPaths.isEmpty()){
            matchingNodes = new ArrayList<>();
            return;
        }

        // flatten the matching paths to a single list, and create a new tree with only the matching nodes
        Node<?> newRoot = new Node<>(null, "Root", AppLogic.getRoot());
        matchingNodes.add(newRoot);
        // flatten the list of lists and only add unique nodes
        for (List<Node<?>> path : matchingPaths) {
            for (Node<?> node : path) {
                if (!matchingNodes.contains(node)) {
                    matchingNodes.add(node);
                }
            }
        }

    }


    public void setNodeClickListener(TreeListener listener) {
        tree.setNodeClickListener(listener);
    }

}