package org.tuiasi.engine.ui.uiWindows.prefabs;

import imgui.ImVec2;
import org.tuiasi.engine.ui.components.basicComponents.tree.TreeNode;
import org.tuiasi.engine.ui.components.composedComponents.TreeWithTitleAndSearchBar;
import org.tuiasi.engine.ui.uiWindows.UIWindow;

import java.util.ArrayList;
import java.util.List;

public class UINodeTreeWindow extends UIWindow {
    public UINodeTreeWindow(String windowTitle) {
        super(windowTitle);
        addPrefabComponents();
    }

    public UINodeTreeWindow(String windowTitle, ImVec2 relativePosition, ImVec2 size) {
        super(windowTitle, relativePosition, size);
        addPrefabComponents();
    }

    public UINodeTreeWindow(String windowTitle, ImVec2 relativePosition, ImVec2 size, boolean isRootWindow) {
        super(windowTitle, relativePosition, size, isRootWindow);
        addPrefabComponents();
    }

    @Override
    protected void addPrefabComponents(){
        TreeWithTitleAndSearchBar tree = new TreeWithTitleAndSearchBar("Search", "Nodes", new ArrayList<>(), null, null);
        tree.addNode(new TreeNode("Node 1", new ArrayList<>(List.of(new TreeNode("Node 1.1"), new TreeNode("Node 1.2")))));
        tree.addNode(new TreeNode("Node 2"));
        tree.addNode(new TreeNode("Node 3"));
        addComponent(tree);
    }

    @Override
    protected void configurePrefabComponents(){

    }



}
