package org.tuiasi.engine.ui.uiWindows.prefabs;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.type.ImString;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.tuiasi.engine.global.nodes.Node;
import org.tuiasi.engine.logic.AppLogic;
import org.tuiasi.engine.ui.components.basicComponents.button.Button;
import org.tuiasi.engine.ui.components.basicComponents.checkbox.Checkbox;
import org.tuiasi.engine.ui.components.basicComponents.checkbox.CheckboxListener;
import org.tuiasi.engine.ui.components.basicComponents.checkbox.CheckboxWithTitle;
import org.tuiasi.engine.ui.components.basicComponents.dropdown.DropdownListener;
import org.tuiasi.engine.ui.components.basicComponents.dropdown.DropdownWithTitle;
import org.tuiasi.engine.ui.components.basicComponents.label.Label;
import org.tuiasi.engine.ui.components.basicComponents.searchbar.SearchListener;
import org.tuiasi.engine.ui.components.basicComponents.searchbar.SearchbarWithHint;
import org.tuiasi.engine.ui.uiWindows.UIWindow;

import java.util.ArrayList;
import java.util.List;

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

            // Vector2f
            if(selectedNode.getFieldValue(name) instanceof Vector2f) {
                Vector3f value = (Vector3f) selectedNode.getFieldValue(name);
                for (int j = 0; j < 2; j++) {
                    // add a label saying x,y,z,w depending on the j value
                    Label compLabel = new Label(String.valueOf((char)('x' + j)), true, 16);
                    addComponent(compLabel);
                    SearchbarWithHint field = new SearchbarWithHint(name + " " + j, name + " " + j, false);
                    int finalJ = j;
                    field.setSearchListener(new SearchListener() {
                        @Override
                        public void onSearch(String searchText) {
                            // depending on the j value, set the x, y or z value of the vector
                            try {
                                if (finalJ == 0) {
                                    value.x = Float.parseFloat(searchText);
                                } else {
                                    value.y = Float.parseFloat(searchText);
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid number format");
                            }
                        }
                    });

                    if(j == 1)
                        field.setSeparator(true);
                    field.setSearchText(new ImString(String.valueOf(value.get(j)), 50));
                    addComponent(field);
                }
            }
            // Vector3f
            else if(selectedNode.getFieldValue(name) instanceof Vector3f){
                Vector3f value = (Vector3f) selectedNode.getFieldValue(name);
                for(int j = 0; j < 3; j++) {
                    // add a label saying x,y,z,w depending on the j value
                    Label compLabel = new Label(String.valueOf((char)('x' + j)), true, 16);
                    addComponent(compLabel);
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
                                } else {
                                    value.z = Float.parseFloat(searchText);
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid number format");
                            }
                        }
                    });

                    if(j == 2)
                        field.setSeparator(true);
                    field.setSearchText(new ImString(String.valueOf(value.get(j)), 50));
                    addComponent(field);
                }
            }
            // Vector4f
            else if(selectedNode.getFieldValue(name) instanceof Vector4f){
                Vector4f value = (Vector4f) selectedNode.getFieldValue(name);
                for(int j = 0; j < 4; j++) {
                    // add a label saying x,y,z,w depending on the j value
                    Label compLabel = new Label(String.valueOf((char)('x' + j)), true, 16);
                    addComponent(compLabel);
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
                                } else {
                                    value.w = Float.parseFloat(searchText);
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid number format");
                            }
                        }
                    });

                    if(j == 3)
                        field.setSeparator(true);
                    field.setSearchText(new ImString(String.valueOf(value.get(j)), 50));
                    addComponent(field);
                }
            }
            // Numeric
            else if(selectedNode.getFieldValue(name) instanceof Integer || selectedNode.getFieldValue(name) instanceof Float || selectedNode.getFieldValue(name) instanceof Double){
                Number value = (Number) selectedNode.getFieldValue(name);
                SearchbarWithHint field = new SearchbarWithHint(name, name, false);
                field.setSeparator(true);
                field.setSearchListener(new SearchListener() {
                    @Override
                    public void onSearch(String searchText) {
                        try {
                            if (selectedNode.getFieldValue(name) instanceof Integer) {
                                selectedNode.setFieldValue(name, Integer.parseInt(searchText));
                            } else if (selectedNode.getFieldValue(name) instanceof Float) {
                                selectedNode.setFieldValue(name, Float.parseFloat(searchText));
                            } else {
                                selectedNode.setFieldValue(name, Double.parseDouble(searchText));
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid number format");
                        }
                    }
                });

                field.setSearchText(new ImString(String.valueOf(value), 50));
                addComponent(field);
            }
            // Boolean
            else if(selectedNode.getFieldValue(name) instanceof Boolean){
                Boolean value = (Boolean) selectedNode.getFieldValue(name);
                Checkbox checkbox = new Checkbox(name, value);
                checkbox.setListener(new CheckboxListener() {
                    @Override
                    public void onToggle(boolean isChecked) {
                        selectedNode.setFieldValue(name, isChecked);
                    }
                });
                addComponent(checkbox);
            }
            // Enums
            else if(selectedNode.getFieldValue(name).getClass().isEnum()){
                // get all the values from the enum as strings
                String[] enumValues = new String[selectedNode.getFieldValue(name).getClass().getEnumConstants().length];
                int index = 0;
                for(Object enumValue : selectedNode.getFieldValue(name).getClass().getEnumConstants()){
                    enumValues[index++] = enumValue.toString();
                }
                DropdownWithTitle dropdown = new DropdownWithTitle(name, enumValues);
                dropdown.setSelectedItemIndex(((Enum)selectedNode.getFieldValue(name)).ordinal());
                dropdown.setWidth(200);
                dropdown.setSeparator(true);

                dropdown.setListener(new DropdownListener() {
                    @Override
                    public void onItemSelected(int index) {
                        selectedNode.setFieldValue(name, selectedNode.getFieldValue(name).getClass().getEnumConstants()[index]);
                    }
                });

                addComponent(dropdown);
            }
            // String
            else if(selectedNode.getFieldValue(name) instanceof String){
                String value = (String) selectedNode.getFieldValue(name);
                SearchbarWithHint field = new SearchbarWithHint(name, name, false);
                field.setSeparator(true);
                field.setSearchListener(new SearchListener() {
                    @Override
                    public void onSearch(String searchText) {
                        selectedNode.setFieldValue(name, searchText);
                    }
                });

                field.setSearchText(new ImString(value, 50));
                addComponent(field);
            }
            // Everything else uses a searchbar with hint
            else {
                SearchbarWithHint field = new SearchbarWithHint(name, name, false);
                field.setSeparator(true);
                field.setSearchListener(new SearchListener() {
                    @Override
                    public void onSearch(String searchText) {
                        selectedNode.setFieldValue(name, searchText);
                    }
                });

                field.setSearchText(new ImString(selectedNode.getFieldValue(name).toString(), 50));
                addComponent(field);
            }


        }

    }

}
