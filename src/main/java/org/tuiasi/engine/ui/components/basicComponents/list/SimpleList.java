package org.tuiasi.engine.ui.components.basicComponents.list;


import imgui.ImGui;
import imgui.flag.ImGuiCol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleList extends IList{
    private String label;
    private List<String> items;

    private Integer lastClickedItemId;  // Variable to store the last clicked node
    ListListener itemClickListener;

    public SimpleList(String label, List<String> items) {
        this.label = label;
        this.items = items;
    }

    @Override
    public void render() {
        // render the list like the tree, but flat and clickable
        for (String item : items) {
            // Check if the current node is the last clicked one
            boolean isLastClickedItem = lastClickedItemId != null && lastClickedItemId.equals(item.hashCode());

            // Highlight the last clicked node
            if (isLastClickedItem) {
                ImGui.pushStyleColor(ImGuiCol.Text, 1.0f, 1.0f, 0.0f, 1.0f);  // Yellow text color
            }

            if (ImGui.selectable(item + "##Selectable")) {
                lastClickedItemId = item.hashCode();  // Update the last clicked node ID
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(item);
                }
            }

            // Restore the text color if not the last clicked node
            if (isLastClickedItem) {
                ImGui.popStyleColor();
            }

        }
    }

}
