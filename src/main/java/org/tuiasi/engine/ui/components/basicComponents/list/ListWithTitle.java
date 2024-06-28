package org.tuiasi.engine.ui.components.basicComponents.list;

import imgui.ImGui;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ListWithTitle extends SimpleList{
    public ListWithTitle(String label, List<String> items) {
        super(label, items);
    }

    @Override
    public void render() {
        ImGui.text(getLabel());
        super.render();
    }


}