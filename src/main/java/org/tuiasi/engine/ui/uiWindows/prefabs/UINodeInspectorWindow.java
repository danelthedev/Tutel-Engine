package org.tuiasi.engine.ui.uiWindows.prefabs;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.type.ImString;
import org.joml.Vector3f;
import org.tuiasi.engine.global.nodes.Node;
import org.tuiasi.engine.logic.AppLogic;
import org.tuiasi.engine.ui.components.basicComponents.button.Button;
import org.tuiasi.engine.ui.components.basicComponents.checkbox.CheckboxWithTitle;
import org.tuiasi.engine.ui.components.basicComponents.label.Label;
import org.tuiasi.engine.ui.components.basicComponents.searchbar.SearchListener;
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
        System.out.println("Refreshing inspector");
        clearComponents();
        Node<?> selectedNode = AppLogic.getSelectedNode();
        // iterate over all the fields of the selected node and add labels and input fields for each field
        for(int i = 0; i < selectedNode.getFields().size(); i++) {
            String name = selectedNode.getFields().get(i);
            Label label = new Label(name, false, 16);
            addComponent(label);

            if(selectedNode.getFieldValue(name) instanceof Vector3f) {
                Vector3f value = (Vector3f) selectedNode.getFieldValue(name);
                for(int j = 0; j < 3; j++) {
                    SearchbarWithHint field = new SearchbarWithHint(name + " " + j, name + " " + j, false);

                    int finalJ = j;
                    field.setSearchListener(new SearchListener() {
                        @Override
                        public void onSearch(String searchText) {
                            // depending on the j value, set the x, y or z value of the vector
                            try {
                                if (finalJ == 0) {
                                    value.x = Float.parseFloat(searchText);
                                } else if (finalJ == 1) {
                                    value.y = Float.parseFloat(searchText);
                                } else if (finalJ == 2) {
                                    value.z = Float.parseFloat(searchText);
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid number format");
                            }
                        }
                    });

                    field.setSearchText(new ImString(String.valueOf(value.get(j)), 50));
                    addComponent(field);
                }
            }
            else {
                SearchbarWithHint field = new SearchbarWithHint(name, name, false);
                addComponent(field);
            }

        }

    }

}
