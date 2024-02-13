package org.tuiasi.engine.ui.basicComponents.tree;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiTreeNodeFlags;
import lombok.*;

import java.util.List;

@Setter @Getter @RequiredArgsConstructor
public class TreeWithTitle extends Tree{

    private String title;

    @Override
    public void render() {
        ImGui.text(title);
        ImGui.separator();
        super.render();
    }
}
