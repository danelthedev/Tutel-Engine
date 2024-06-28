package org.tuiasi.engine.ui.components.basicComponents.checkbox;

import imgui.ImGui;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class CheckboxWithTitle extends Checkbox {

    private String label;
    private boolean isChecked;

    private CheckboxListener listener;

    public CheckboxWithTitle(String label, boolean isChecked) {
        this.label = label;
        this.isChecked = isChecked;
    }

    @Override
    public void render() {
        ImGui.checkbox( label + "##Checkbox", isChecked);

        if (ImGui.isItemClicked()) {
            isChecked = !isChecked;
            if(listener != null) {
                listener.onToggle(isChecked);
            }
        }

    }

    public boolean isChecked() {
        return isChecked;
    }
}