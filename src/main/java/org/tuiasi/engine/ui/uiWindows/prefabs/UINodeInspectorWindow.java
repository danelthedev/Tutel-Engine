package org.tuiasi.engine.ui.uiWindows.prefabs;

import imgui.ImVec2;
import org.tuiasi.engine.ui.components.basicComponents.button.Button;
import org.tuiasi.engine.ui.components.basicComponents.checkbox.CheckboxWithTitle;
import org.tuiasi.engine.ui.components.basicComponents.searchbar.SearchbarWithHint;
import org.tuiasi.engine.ui.components.basicComponents.textbox.Textbox;
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
        Button button = new Button("Button");
        addComponent(button);
        CheckboxWithTitle checkbox = new CheckboxWithTitle("Checkbox", false);
        addComponent(checkbox);
        SearchbarWithHint searchbar = new SearchbarWithHint("TextboxInspector", "Search");
        addComponent(searchbar);
    }

    @Override
    protected void configurePrefabComponents(){
        Button button = (Button) getComponentByLabel("Button");
        button.setSize(1f, .1f);
    }

}
