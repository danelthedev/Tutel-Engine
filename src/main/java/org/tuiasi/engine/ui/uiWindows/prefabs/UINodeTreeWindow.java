package org.tuiasi.engine.ui.uiWindows.prefabs;

import imgui.ImVec2;
import org.lwjgl.glfw.GLFW;
import org.tuiasi.engine.global.nodes.physics.body.KinematicBody;
import org.tuiasi.engine.global.nodes.physics.collider.Collider3D;
import org.tuiasi.engine.logic.IO.KeyboardHandler;
import org.tuiasi.engine.global.nodes.Node;
import org.tuiasi.engine.logic.AppLogic;
import org.tuiasi.engine.logic.logger.Log;
import org.tuiasi.engine.renderer.Renderer;
import org.tuiasi.engine.renderer.light.LightSource;
import org.tuiasi.engine.renderer.renderable.Renderable3D;
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
        addNodeButton.setRatioedPosition(0.05f, 0.04f);
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

        // Add key listener for delete key
        if (KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_DELETE)) {
            Node<?> selectedNode = AppLogic.getSelectedNode();
            if (selectedNode != null && selectedNode.getParent() != null) {
                deleteNodeAndChildren(selectedNode);
                AppLogic.setSelectedNode(null); // Clear the selected node
            }
        }
    }

    public void deleteNodeAndChildren(Node<?> node) {
        if (node.getValue() instanceof Renderable3D) {
            Renderer.removeRenderable((Renderable3D) node.getValue());
        }
        else if (node.getValue() instanceof LightSource) {
            Renderer.removeLightSource((LightSource) node.getValue());
        }
        else if(node.getValue() instanceof Collider3D) {
            Renderer.removeRenderable(((Collider3D) node.getValue()).getRepresentation());
        }

        for (int i = node.getChildren().size() - 1; i >= 0; i--) {
            Node<?> child = node.getChildren().get(i);
            deleteNodeAndChildren(child);
        }

        node.getParent().removeChild(node);
    }

}
