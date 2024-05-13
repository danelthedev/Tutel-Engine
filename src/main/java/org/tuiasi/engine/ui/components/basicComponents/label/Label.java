package org.tuiasi.engine.ui.components.basicComponents.label;

import imgui.ImGui;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tuiasi.engine.ui.AppWindow;

@Setter @Getter @NoArgsConstructor
public class Label extends ILabel {

    private String label;
    private boolean isInline = false;
    private int fontSize = 16;

    public Label(String label) {
        this.label = label;
    }

    public Label(String label, boolean isInline, int fontSize) {
        this.label = label;
        this.isInline = isInline;
        this.fontSize = fontSize;
    }

    @Override
    public void render() {
        ImGui.pushFont(AppWindow.appFonts.get(fontSize));
        setWidth(ImGui.calcTextSize(label).x);
        setHeight(ImGui.calcTextSize(label).y);

        if(getRatioX() != 0 && getRatioY() != 0 && getWidth() != 0 && getHeight() != 0) {
            ImGui.setCursorPosX((ImGui.getWindowSizeX() - getWidth()) * getRatioX());
            ImGui.setCursorPosY((ImGui.getWindowSizeY() - getHeight()) * getRatioY());
        }

        ImGui.text(label);
        ImGui.popFont();

        if(isInline)
            ImGui.sameLine();
        else
            ImGui.newLine();
    }

}