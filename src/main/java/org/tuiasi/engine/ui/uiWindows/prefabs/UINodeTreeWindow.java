package org.tuiasi.engine.ui.uiWindows.prefabs;

import imgui.ImVec2;
import org.tuiasi.engine.global.nodes.Node;
import org.tuiasi.engine.logic.AppLogic;
import org.tuiasi.engine.ui.DefaultEngineEditorUI;
import org.tuiasi.engine.ui.components.basicComponents.button.Button;
import org.tuiasi.engine.ui.components.basicComponents.tree.TreeListener;
import org.tuiasi.engine.ui.components.composedComponents.TreeWithTitleAndSearchBar;
import org.tuiasi.engine.ui.uiWindows.UIWindow;

public class UINodeTreeWindow extends UIWindow {

    Button addNodeButton;
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

        addNodeButton = new Button("Add Node", () -> {
            if(AppLogic.getSelectedNode() != null){
                UINewNodeModalWindow newNodeModalWindow = new UINewNodeModalWindow("New Node", new ImVec2(0, 0), new ImVec2(100, 100));
                newNodeModalWindow.setDocked(false);
                DefaultEngineEditorUI.addWindow(newNodeModalWindow);
            }
        });
        addNodeButton.setSize(100, 20);
        addNodeButton.setRatioedPosition(0.05f, 0.05f);
        addNodeButton.setSeparator(true);

        addComponent(addNodeButton);

        treeComponent = new TreeWithTitleAndSearchBar("Search", "Nodes", null, new TreeListener() {
            @Override
            public void onNodeClick(Node<?> node) {
                AppLogic.setSelectedNode(node);

                UINodeInspectorWindow nodeInspectorWindow = ((UINodeInspectorWindow)DefaultEngineEditorUI.getWindow("Node Inspector"));
                if(nodeInspectorWindow != null){
                    nodeInspectorWindow.refresh();
                }

            }
        }, null);
        addComponent(treeComponent);

    }

    @Override
    protected void configurePrefabComponents(){

    }

    @Override
    public void render() {
        treeComponent.getTree().setRoot(AppLogic.getRoot());
        super.render();
    }


}
