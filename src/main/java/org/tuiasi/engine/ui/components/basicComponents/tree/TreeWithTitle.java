package org.tuiasi.engine.ui.components.basicComponents.tree;

import imgui.ImGui;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
