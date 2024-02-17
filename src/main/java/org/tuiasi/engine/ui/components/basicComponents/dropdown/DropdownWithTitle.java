package org.tuiasi.engine.ui.components.basicComponents.dropdown;

import imgui.ImGui;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DropdownWithTitle implements IDropdown{

    private String label;
    private String[] items;
    private int selectedItemIndex;
    private DropdownListener listener;

    public DropdownWithTitle(String label, String[] items) {
        this.label = label;
        this.items = items;
        this.selectedItemIndex = 0; // Default to the first item
    }

    @Override
    public void render() {
        // Render the label
        ImGui.text(label);
        // Render the dropdown
        if (ImGui.beginCombo(label + "##Dropdown", items[selectedItemIndex])) {
            for (int i = 0; i < items.length; i++) {
                boolean isSelected = selectedItemIndex == i;
                if (ImGui.selectable(items[i] + "##OptionOfDropdown_" + label, isSelected)) {
                    selectedItemIndex = i;
                    if (listener != null) {
                        listener.onItemSelected(i);
                    }
                }
                if (isSelected) {
                    ImGui.setItemDefaultFocus();
                }
            }
            ImGui.endCombo();
        }
    }

}
