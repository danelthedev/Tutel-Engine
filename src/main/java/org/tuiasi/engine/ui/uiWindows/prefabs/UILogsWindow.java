package org.tuiasi.engine.ui.uiWindows.prefabs;

import imgui.ImVec2;
import imgui.flag.ImGuiInputTextFlags;
import lombok.Getter;
import org.tuiasi.engine.ui.components.basicComponents.button.Button;
import org.tuiasi.engine.ui.components.basicComponents.textbox.Textbox;
import org.tuiasi.engine.ui.uiWindows.UIWindow;

public class UILogsWindow extends UIWindow {

    @Getter
    private Textbox logsTextbox;

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
        logsTextbox = new Textbox("Logs");
        logsTextbox.addFlag(ImGuiInputTextFlags.ReadOnly);
        addComponent(logsTextbox);
    }

    @Override
    protected void configurePrefabComponents(){
        Textbox logsTextbox = (Textbox) getComponentByLabel("Logs");
    }

}
