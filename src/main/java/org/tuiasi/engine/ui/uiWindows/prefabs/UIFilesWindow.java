package org.tuiasi.engine.ui.uiWindows.prefabs;

import imgui.ImVec2;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tuiasi.engine.global.nodes.Node;
import org.tuiasi.engine.logic.AppLogic;
import org.tuiasi.engine.ui.DefaultEngineEditorUI;
import org.tuiasi.engine.ui.components.basicComponents.list.SimpleList;
import org.tuiasi.engine.ui.components.basicComponents.tree.TreeListener;
import org.tuiasi.engine.ui.components.composedComponents.TreeWithTitleAndSearchBar;
import org.tuiasi.engine.ui.uiWindows.UIWindow;

import java.util.List;

@Getter
@Setter
public class UIFilesWindow extends UIWindow {

    TreeWithTitleAndSearchBar treeComponent;

    public UIFilesWindow(String windowTitle) {
        super(windowTitle);
        addPrefabComponents();
    }

    public UIFilesWindow(String windowTitle, ImVec2 relativePosition, ImVec2 size) {
        super(windowTitle, relativePosition, size);
        addPrefabComponents();
    }

    public UIFilesWindow(String windowTitle, ImVec2 relativePosition, ImVec2 size, boolean isRootWindow) {
        super(windowTitle, relativePosition, size, isRootWindow);
        addPrefabComponents();
    }

    @Override
    protected void addPrefabComponents(){
        treeComponent = new TreeWithTitleAndSearchBar("Search", "Nodes", null, new TreeListener() {
            @Override
            public void onNodeClick(Node<?> node) {

            }
        }, null);
        addComponent(treeComponent);
    }

    @Override
    protected void configurePrefabComponents(){
    }

    @Override
    public void render() {
        super.render();
    }

}
