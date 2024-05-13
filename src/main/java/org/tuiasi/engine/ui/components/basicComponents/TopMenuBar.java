package org.tuiasi.engine.ui.components.basicComponents;

import imgui.flag.ImGuiStyleVar;
import imgui.internal.ImGui;
import org.tuiasi.engine.global.WindowVariables;
import org.tuiasi.engine.ui.components.IComponent;

public class TopMenuBar extends IComponent {

    public String getLabel() {
        return "TopMenuBar";
    }

    @Override
    public void setLabel(String label) {
    }

    @Override
    public void render() {

        // top menu bar with 2 options: file and edit

        ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 0, 10);
        ImGui.beginMainMenuBar();

        WindowVariables.getInstance().setMainMenuHeight(ImGui.getWindowSize().y);

        if (ImGui.beginMenu("File")) {
            ImGui.endMenu();
        }
        if (ImGui.beginMenu("Edit")) {
            ImGui.endMenu();
        }
        ImGui.endMainMenuBar();
        ImGui.popStyleVar();
    }
}
