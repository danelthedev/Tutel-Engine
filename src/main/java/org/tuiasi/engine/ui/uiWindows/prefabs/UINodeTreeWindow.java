package org.tuiasi.engine.ui.uiWindows.prefabs;

import imgui.ImVec2;
import org.tuiasi.engine.logic.AppLogic;
import org.tuiasi.engine.ui.components.composedComponents.TreeWithTitleAndSearchBar;
import org.tuiasi.engine.ui.uiWindows.UIWindow;

public class UINodeTreeWindow extends UIWindow {

    TreeWithTitleAndSearchBar treeComponent;

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
        treeComponent = new TreeWithTitleAndSearchBar("Search", "Nodes", null, null, null);
        addComponent(treeComponent);

    }

    @Override
    protected void configurePrefabComponents(){

    }

    @Override
    public void render() {
//        if(treeComponent.getTree().getRoot() == null)
        treeComponent.getTree().setRoot(AppLogic.getRoot());

        super.render();
    }


}
