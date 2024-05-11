package org.tuiasi.engine.ui.components.composedComponents;

import imgui.ImGui;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tuiasi.engine.global.nodes.Node;
import org.tuiasi.engine.ui.components.IComponent;
import org.tuiasi.engine.ui.components.basicComponents.searchbar.SearchListener;
import org.tuiasi.engine.ui.components.basicComponents.searchbar.SearchbarWithHint;
import org.tuiasi.engine.ui.components.basicComponents.tree.Tree;
import org.tuiasi.engine.ui.components.basicComponents.tree.TreeListener;

@Getter
@Setter
@NoArgsConstructor
public class TreeWithTitleAndSearchBar extends IComponent {

    private String label;
    private SearchbarWithHint searchbar;
    private Tree tree;

    public TreeWithTitleAndSearchBar(String hint, String label, Node root, TreeListener nodeClickListener, SearchListener searchListener) {
        this.label = label;

        this.searchbar = new SearchbarWithHint(hint);
        this.searchbar.setSearchListener(searchListener);

        this.tree = new Tree(root, nodeClickListener);
    }

    public void render() {
        ImGui.text(label);

        searchbar.render();
        ImGui.newLine();
        ImGui.separator();

        // Filter tree data based on the search text
        if (searchbar.isEnterPressed()) {
            filterTreeData();
            searchbar.setEnterPressed(false); // Reset the flag
        }

        tree.render();
    }


    private void filterTreeData() {

    }

    public void setNodeClickListener(TreeListener listener) {
        tree.setNodeClickListener(listener);
    }

}