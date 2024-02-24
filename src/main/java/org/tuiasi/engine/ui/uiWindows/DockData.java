package org.tuiasi.engine.ui.uiWindows;

import imgui.flag.ImGuiDir;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor  @AllArgsConstructor
public class DockData {
    private int direction;
    private float splitRatio;
}
