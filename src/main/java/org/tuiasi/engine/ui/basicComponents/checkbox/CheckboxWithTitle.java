package org.tuiasi.engine.ui.basicComponents.checkbox;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class CheckboxWithTitle implements ICheckbox {

    private String title;
    private boolean isChecked;

    private CheckboxListener listener;

    public CheckboxWithTitle(String title) {
        this.title = title;
        this.isChecked = false;
    }

    @Override
    public void render() {
        // Render the checkbox
        ImGui.checkbox(title, isChecked);

        if (ImGui.isItemClicked()) {
            isChecked = !isChecked;
            if(listener != null) {
                listener.onToggle(isChecked);
            }
        }

    }


    // Getter to retrieve the checkbox state
    public boolean isChecked() {
        return isChecked;
    }
}