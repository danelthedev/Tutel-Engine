package org.tuiasi.engine.ui.uiWindows.prefabs;

import imgui.ImVec2;
import org.tuiasi.engine.global.nodes.Node;
import org.tuiasi.engine.logic.AppLogic;
import org.tuiasi.engine.ui.components.basicComponents.button.Button;
import org.tuiasi.engine.ui.components.basicComponents.checkbox.CheckboxWithTitle;
import org.tuiasi.engine.ui.components.basicComponents.searchbar.SearchbarWithHint;
import org.tuiasi.engine.ui.uiWindows.UIWindow;

public class UINodeInspectorWindow extends UIWindow {

    public UINodeInspectorWindow(String windowTitle) {
        super(windowTitle);
        addPrefabComponents();
    }

    public UINodeInspectorWindow(String windowTitle, ImVec2 relativePosition, ImVec2 size) {
        super(windowTitle, relativePosition, size);
        addPrefabComponents();
    }

    public UINodeInspectorWindow(String windowTitle, ImVec2 relativePosition, ImVec2 size, boolean isRootWindow) {
        super(windowTitle, relativePosition, size, isRootWindow);
        addPrefabComponents();
    }

    @Override
    protected void addPrefabComponents(){

    }

    @Override
    protected void configurePrefabComponents(){
    }

    public void refresh(){
        // get the selected node from the AppLogic
        clearComponents();
        Node<?> selectedNode = AppLogic.getSelectedNode();
        // iterate over all the fields of the selected node and add labels and input fields for each field
        for(String name: selectedNode.getFields()) {
            addComponent(new SearchbarWithHint(name, name));
        }
    }

}
