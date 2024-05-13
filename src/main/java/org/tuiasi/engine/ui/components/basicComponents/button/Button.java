package org.tuiasi.engine.ui.components.basicComponents.button;

import imgui.ImGui;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Button extends IButton{

    private String label;
    private ButtonListener listener;


    public Button(String label) {
        this.label = label;
    }

    @Override
    public void render() {
        ImGui.setCursorPosX((ImGui.getWindowSizeX() - getWidth()) * getRatioX());
        ImGui.setCursorPosY((ImGui.getWindowSizeY() - getHeight()) * getRatioY());

        ImGui.button(label + "##Button", getWidth(), getHeight());
        if (ImGui.isItemClicked(0)) {
            if (listener != null) {
                listener.onClick();
            }
        }
    }

}
