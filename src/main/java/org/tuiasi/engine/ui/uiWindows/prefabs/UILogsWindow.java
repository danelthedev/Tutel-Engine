package org.tuiasi.engine.ui.uiWindows.prefabs;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import org.tuiasi.engine.ui.components.basicComponents.textbox.Textbox;
import org.tuiasi.engine.ui.uiWindows.UIWindow;

public class UILogsWindow extends UIWindow {

    public UILogsWindow(String windowTitle) {
        super(windowTitle);
        addPrefabComponents();
    }

    public UILogsWindow(String windowTitle, ImVec2 relativePosition, ImVec2 size) {
        super(windowTitle, relativePosition, size);
        addPrefabComponents();
    }

    public UILogsWindow(String windowTitle, ImVec2 relativePosition, ImVec2 size, boolean isRootWindow) {
        super(windowTitle, relativePosition, size, isRootWindow);
        addPrefabComponents();
    }

    @Override
    protected void addPrefabComponents(){
        Textbox textbox = new Textbox("Logs");
        textbox.addFlag(ImGuiInputTextFlags.ReadOnly);
        addComponent(textbox);
    }

    @Override
    protected void configurePrefabComponents(){
        Textbox logsTextbox = (Textbox) getComponentByLabel("Logs");
        logsTextbox.setSize(1f, .8f);
    }

}
