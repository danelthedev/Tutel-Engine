package org.tuiasi.engine.ui.uiWindows.prefabs;

import imgui.ImVec2;
import org.tuiasi.engine.ui.components.basicComponents.list.SimpleList;
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
        SimpleList filesList = new SimpleList("Logs", List.of("file1", "file2", "file3"));
        addComponent(filesList);
    }

    @Override
    protected void configurePrefabComponents(){
    }
}
