package org.tuiasi.engine.ui.basicComponents.dropdown;

import imgui.ImGui;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DropdownWithTitle implements IDropdown{

    private String title;
    private String[] items;
    private int selectedItemIndex;
    private DropdownListener listener;

    public DropdownWithTitle(String title, String[] items) {
        this.title = title;
        this.items = items;
        this.selectedItemIndex = 0; // Default to the first item
    }

    @Override
    public void render() {
        // Render the title
        ImGui.text(title);
        // Render the dropdown
        if (ImGui.beginCombo("##combo", items[selectedItemIndex])) {
            for (int i = 0; i < items.length; i++) {
                boolean isSelected = selectedItemIndex == i;
                if (ImGui.selectable(items[i], isSelected)) {
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
