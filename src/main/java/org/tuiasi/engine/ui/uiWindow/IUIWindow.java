package org.tuiasi.engine.ui.uiWindow;

import imgui.ImVec2;
import org.lwjgl.bgfx.BGFXInstanceDataBuffer;
import org.tuiasi.engine.ui.components.IComponent;

import java.util.List;

public interface IUIWindow {

    void render();

    void setWindowTitle(String title);
    void setFlags(int flags);
    void addFlag(int flag);
    void removeFlag(int flag);

    String getWindowTitle();
    List<IComponent> getComponents();

    boolean isWindowBeingDragged();
    boolean isDragged();
    ImVec2 getWindowPosition();
    ImVec2 calculateWindowPosition();

}
