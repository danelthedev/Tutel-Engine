package org.tuiasi.engine.ui.components.basicComponents.dropdown;

import imgui.ImGui;
import imgui.flag.ImGuiHoveredFlags;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DropdownWithTitle extends IDropdown{

    private String label;
    private String[] items;
    private String[] descriptions;
    private int selectedItemIndex;
    private DropdownListener listener;

    private float xRatioToWindow = 0f;

    public DropdownWithTitle(String label, String[] items) {
        this.label = label;
        this.items = items;
        this.descriptions = null;
        this.selectedItemIndex = 0;
    }

    public DropdownWithTitle(String label, String[] items, String[] descriptions) {
        this.label = label;
        this.items = items;
        this.descriptions = descriptions;
        this.selectedItemIndex = 0;
    }

    public DropdownWithTitle(String label, String[] items, String[] descriptions, int selectedItemIndex) {
        this.label = label;
        this.items = items;
        this.descriptions = descriptions;
        this.selectedItemIndex = selectedItemIndex;
    }

    public DropdownWithTitle(String label, String[] items, String[] descriptions, int selectedItemIndex, DropdownListener listener) {
        this.label = label;
        this.items = items;
        this.descriptions = descriptions;
        this.selectedItemIndex = selectedItemIndex;
        this.listener = listener;
    }


    @Override
    public void render() {
        if(xRatioToWindow != 0f)
            setWidth(ImGui.getWindowSizeX() * xRatioToWindow);

        ImGui.setNextItemWidth(getWidth());

        if(getRatioX() != 0 && getRatioY() != 0 && getWidth() != 0 && getHeight() != 0) {
            ImGui.setCursorPosX((ImGui.getWindowSizeX() - getWidth()) * getRatioX());
            ImGui.setCursorPosY((ImGui.getWindowSizeY() - getHeight()) * getRatioY());
        }

        if (ImGui.beginCombo(label + "##Dropdown", items[selectedItemIndex])) {

            for (int i = 0; i < items.length; i++) {

                // Render the dropdown
                if(ImGui.isItemHovered(ImGuiHoveredFlags.AllowWhenBlockedByActiveItem | ImGuiHoveredFlags.AllowWhenDisabled
                | ImGuiHoveredFlags.AllowWhenOverlapped)) {
                    // if tooltips are available, show them
                    if(descriptions != null && i < descriptions.length)
                        ImGui.setTooltip(descriptions[i-1]);
                }

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

            if(ImGui.isItemHovered(ImGuiHoveredFlags.AllowWhenBlockedByActiveItem | ImGuiHoveredFlags.AllowWhenDisabled
                    | ImGuiHoveredFlags.AllowWhenOverlapped)) {
                if(descriptions != null)
                    ImGui.setTooltip(descriptions[descriptions.length - 1]);
            }

            ImGui.endCombo();

        }
        if(isSeparator())
            ImGui.separator();
    }

}
