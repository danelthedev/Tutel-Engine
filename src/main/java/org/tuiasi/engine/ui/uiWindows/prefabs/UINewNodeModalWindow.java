package org.tuiasi.engine.ui.uiWindows.prefabs;

import imgui.ImVec2;
import imgui.internal.ImGuiDockNode;
import imgui.internal.flag.ImGuiDockNodeFlags;
import org.tuiasi.engine.global.nodes.Node;
import org.tuiasi.engine.global.nodes.spatial.Spatial;
import org.tuiasi.engine.global.nodes.spatial.Spatial2D;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;
import org.tuiasi.engine.logic.AppLogic;
import org.tuiasi.engine.renderer.light.DirectionalLight;
import org.tuiasi.engine.renderer.light.LightSource;
import org.tuiasi.engine.renderer.light.PointLight;
import org.tuiasi.engine.renderer.renderable.Renderable3D;
import org.tuiasi.engine.ui.DefaultEngineEditorUI;
import org.tuiasi.engine.ui.components.basicComponents.button.Button;
import org.tuiasi.engine.ui.components.basicComponents.tree.TreeListener;
import org.tuiasi.engine.ui.components.composedComponents.TreeWithTitleAndSearchBar;
import org.tuiasi.engine.ui.uiWindows.UIWindow;

import java.lang.reflect.Constructor;

public class UINewNodeModalWindow extends UIWindow {

    TreeWithTitleAndSearchBar treeComponent;
    Button addNodeButton;
    Button cancelButton;
    Node selectedNewNode;


    public UINewNodeModalWindow(String windowTitle) {
        super(windowTitle);
    }

    public UINewNodeModalWindow(String windowTitle, ImVec2 relativePosition, ImVec2 size) {
        super(windowTitle, relativePosition, size);
        addPrefabComponents();
    }

    public UINewNodeModalWindow(String windowTitle, ImVec2 relativePosition, ImVec2 size, boolean isRootWindow) {
        super(windowTitle, relativePosition, size, isRootWindow);
        addPrefabComponents();
    }

    @Override
    protected void addPrefabComponents() {
        setSize(new ImVec2(400, 800));
        setFlags(ImGuiDockNodeFlags.NoDocking);

        treeComponent = new TreeWithTitleAndSearchBar("Search", "New node", null, new TreeListener() {
            @Override
            public void onNodeClick(Node<?> node) {
                selectedNewNode = node;
            }
        }, null);

        Node<?> root = new Node<>(null, "Node", Node.class);
        Node<?> spatial = new Node<>(root, "Spatial", Spatial.class);
        Node<?> spatial3D = new Node<>(spatial, "Spatial3D", Spatial3D.class);
        Node<?> spatial2D = new Node<>(spatial, "Spatial2D", Spatial2D.class);
        Node<?> renderable3D = new Node<>(spatial3D, "Renderable3D", Renderable3D.class);
        Node<?> lightSource = new Node<>(root, "Light Source", LightSource.class);
        Node<?> directionalLight = new Node<>(lightSource, "Directional Light", DirectionalLight.class);
        Node<?> pointLight = new Node<>(lightSource, "Point Light", PointLight.class);

        treeComponent.getTree().setRoot(root);


        addComponent(treeComponent);

        addNodeButton = new Button("Add node");
        addNodeButton.setSize(100, 30);
        addNodeButton.setRatioedPosition(0.8f, 0.95f);

        addComponent(addNodeButton);
        addNodeButton.setListener(() -> {
            if(selectedNewNode != null){
                // using reflection create an instance of the class that has the name of the selected node value
                Object value = null;
                try {
                    Constructor<?> constructor = ((Class)selectedNewNode.getValue()).getDeclaredConstructor();
                    value = constructor.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Node<?> newNode = new Node<>(AppLogic.getSelectedNode(), selectedNewNode.getName(), value);
            }
            DefaultEngineEditorUI.removeWindow(this);
        });

        cancelButton = new Button("Cancel");
        cancelButton.setSize(100, 30);
        cancelButton.setRatioedPosition(0.2f, 0.95f);
        cancelButton.setListener(() -> {
            DefaultEngineEditorUI.removeWindow(this);
        });

        addComponent(cancelButton);
    }

    @Override
    protected void configurePrefabComponents(){
    }

    @Override
    public void render() {
        super.render();
    }

}
