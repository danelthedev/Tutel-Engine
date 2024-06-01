package org.tuiasi.engine.ui.uiWindows.prefabs;

import imgui.ImVec2;
import imgui.type.ImString;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.tuiasi.engine.global.nodes.Node;
import org.tuiasi.engine.logic.AppLogic;
import org.tuiasi.engine.logic.codeProcessor.IScript;
import org.tuiasi.engine.logic.codeProcessor.UserScript;
import org.tuiasi.engine.ui.components.basicComponents.checkbox.Checkbox;
import org.tuiasi.engine.ui.components.basicComponents.checkbox.CheckboxListener;
import org.tuiasi.engine.ui.components.basicComponents.dropdown.DropdownListener;
import org.tuiasi.engine.ui.components.basicComponents.dropdown.DropdownWithTitle;
import org.tuiasi.engine.ui.components.basicComponents.label.Label;
import org.tuiasi.engine.ui.components.basicComponents.searchbar.SearchListener;
import org.tuiasi.engine.ui.components.basicComponents.searchbar.SearchbarWithHint;
import org.tuiasi.engine.ui.components.composedComponents.Dialog.DialogType;
import org.tuiasi.engine.ui.components.composedComponents.Dialog.FileDialogFromButton;
import org.tuiasi.engine.ui.uiWindows.UIWindow;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

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
        clearComponents();
        Node<?> selectedNode = AppLogic.getSelectedNode();

        // add a label with the name of the selected node and a textbox that allows the user to change the name
        Label nameLabel = new Label("Name", false, 16);
        addComponent(nameLabel);
        SearchbarWithHint nameField = new SearchbarWithHint("Name", "Name", false);
        nameField.setSearchListener(new SearchListener() {
            @Override
            public void onSearch(String searchText) {
                selectedNode.setName(searchText);
            }
        });
        nameField.setSearchText(new ImString(selectedNode.getName(), 250));
        nameField.setSeparator(true);
        addComponent(nameField);

        // add a label with the word "Script" and a textbox with browse button that allows the user to change the script
        Label scriptLabel = new Label("Script", false, 16);
        addComponent(scriptLabel);
        SearchbarWithHint scriptField = new SearchbarWithHint("Script", "Script", false);
        scriptField.setSearchListener(new SearchListener() {
            @Override
            public void onSearch(String searchText) {
                selectedNode.setScript(searchText);
                scriptField.setSearchText(new ImString(searchText, 250));

                JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                int result = compiler.run(null, null, null, new File(searchText).getPath());

                try {
                    String classPath = searchText.substring(0, searchText.lastIndexOf("\\"));
                    String className = searchText.substring(searchText.lastIndexOf("\\") + 1, searchText.lastIndexOf("."));

                    URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new File(classPath).toURI().toURL()});
                    Class<?> script = Class.forName(className, true, classLoader);
                    UserScript scriptInstance = (UserScript) script.getDeclaredConstructor().newInstance();
                    selectedNode.setScriptObj(scriptInstance);
                    selectedNode.getScriptObj().setRoot(AppLogic.getRoot());
                    scriptInstance.setAttachedNode(selectedNode);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        scriptField.setSearchText(selectedNode.getScript() == null ? new ImString("", 250) : new ImString(selectedNode.getScript(), 250));
        addComponent(scriptField);

        scriptField.setEditable(false);
        FileDialogFromButton scriptBrowse = new FileDialogFromButton("Browse script", DialogType.FILE, scriptField);
        scriptBrowse.setSeparator(true);
        addComponent(scriptBrowse);

        // iterate over all the fields of the selected node and add labels and input fields for each field
        for(int i = 0; i < selectedNode.getFields().size(); i++) {
            String name = selectedNode.getFields().get(i);
            Label label = new Label(name, false, 16);
            addComponent(label);

            if(selectedNode.getFieldValue(name) == null) {
                SearchbarWithHint field = new SearchbarWithHint(name, name, false);
                field.setSeparator(true);
                field.setSearchListener(new SearchListener() {
                    @Override
                    public void onSearch(String searchText) {
                        selectedNode.setFieldValue(name, searchText);
                    }
                });

                ImString displayedValue = selectedNode.getFieldValue(name) != null ? new ImString(selectedNode.getFieldValue(name).toString(), 250) : new ImString("", 250);

                field.setSearchText(displayedValue);
                addComponent(field);
            }else
            // Vector2f
            if(selectedNode.getFieldValue(name) instanceof Vector2f) {
                Vector2f value = (Vector2f) selectedNode.getFieldValue(name);
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
                                    selectedNode.setFieldValue(name, new Vector2f(Float.parseFloat(searchText), value.y));
                                    value.x = Float.parseFloat(searchText);
                                } else {
                                    selectedNode.setFieldValue(name, new Vector2f(value.x, Float.parseFloat(searchText)));
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
                                    selectedNode.setFieldValue(name, new Vector3f(Float.parseFloat(searchText), value.y, value.z));
                                    value.x = Float.parseFloat(searchText);
                                } else if (finalJ == 1) {
                                    selectedNode.setFieldValue(name, new Vector3f(value.x, Float.parseFloat(searchText), value.z));
                                    value.y = Float.parseFloat(searchText);
                                } else {
                                    selectedNode.setFieldValue(name, new Vector3f(value.x, value.y, Float.parseFloat(searchText)));
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
                                    selectedNode.setFieldValue(name, new Vector4f(Float.parseFloat(searchText), value.y, value.z, value.w));
                                    value.x = Float.parseFloat(searchText);
                                } else if (finalJ == 1) {
                                    selectedNode.setFieldValue(name, new Vector4f(value.x, Float.parseFloat(searchText), value.z, value.w));
                                    value.y = Float.parseFloat(searchText);
                                } else if (finalJ == 2) {
                                    selectedNode.setFieldValue(name, new Vector4f(value.x, value.y, Float.parseFloat(searchText), value.w));
                                    value.z = Float.parseFloat(searchText);
                                }else {
                                    selectedNode.setFieldValue(name, new Vector4f(value.x, value.y, value.z, Float.parseFloat(searchText)));
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
                field.setSearchListener(new SearchListener() {
                    @Override
                    public void onSearch(String searchText) {
                        selectedNode.setFieldValue(name, searchText);
                    }
                });

                field.setSearchText(new ImString(value, 250));
                addComponent(field);

                // if the string contains "material" or "mesh" and "path" add a browse button
                if((name.toLowerCase().contains("material") || name.toLowerCase().contains("mesh")) && name.toLowerCase().contains("path")){
                    field.setEditable(false);
                    FileDialogFromButton fileDialogFromButton = new FileDialogFromButton("Browse " + name, DialogType.FILE, field);
                    fileDialogFromButton.setSeparator(true);
                    addComponent(fileDialogFromButton);
                }
                else{
                    field.setSeparator(true);
                }

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

                ImString displayedValue = selectedNode.getFieldValue(name) != null ? new ImString(selectedNode.getFieldValue(name).toString(), 250) : new ImString("", 250);

                field.setSearchText(displayedValue);
                addComponent(field);
            }


        }

    }

}
