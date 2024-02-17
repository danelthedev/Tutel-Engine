package org.tuiasi.engine.ui.components.basicComponents.tree;

import imgui.ImGui;
import lombok.*;

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
