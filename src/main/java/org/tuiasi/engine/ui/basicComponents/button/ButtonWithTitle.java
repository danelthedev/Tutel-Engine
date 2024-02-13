package org.tuiasi.engine.ui.basicComponents.button;

import imgui.ImGui;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ButtonWithTitle implements IButton{

    private String title;
    private ButtonListener listener;


    public ButtonWithTitle(String title) {
        this.title = title;
    }

    @Override
    public void render() {
        ImGui.button(title);
        if (ImGui.isItemClicked(0)) {
            if (listener != null) {
                listener.onClick();
            }
        }
    }

}
