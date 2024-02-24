package org.tuiasi.engine.ui.uiWindows.prefabs;

import imgui.ImVec2;
import imgui.flag.ImGuiInputTextFlags;
import org.tuiasi.engine.ui.components.basicComponents.list.ListWithTitle;
import org.tuiasi.engine.ui.components.basicComponents.textbox.Textbox;
import org.tuiasi.engine.ui.uiWindows.UIWindow;

import java.util.List;

public class UIFilesWindow extends UIWindow {
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
        ListWithTitle filesList = new ListWithTitle("Logs", List.of("file1", "file2", "file3"));
        addComponent(filesList);
    }

    @Override
    protected void configurePrefabComponents(){
    }
}
