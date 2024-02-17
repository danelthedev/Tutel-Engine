package org.tuiasi.engine.ui.components.basicComponents.button;

import imgui.ImGui;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ButtonWithTitle implements IButton{

    private String label;
    private ButtonListener listener;


    public ButtonWithTitle(String label) {
        this.label = label;
    }

    @Override
    public void render() {
        ImGui.button(label + "##Button");
        if (ImGui.isItemClicked(0)) {
            if (listener != null) {
                listener.onClick();
            }
        }
    }

}
