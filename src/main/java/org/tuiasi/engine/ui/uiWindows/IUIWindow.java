package org.tuiasi.engine.ui.uiWindows;

import imgui.ImGui;
import imgui.ImVec2;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tuiasi.engine.global.WindowVariables;
import org.tuiasi.engine.ui.components.IComponent;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter
public abstract class IUIWindow {

    private String windowTitle;
    private int flags = 0;
    private List<IComponent> components = new ArrayList<>();

    private ImVec2 relativePosition;
    private ImVec2 size;

    boolean isFirstTime = true;
    boolean isDocked = false;

    public IUIWindow(String windowTitle) {
        this.windowTitle = windowTitle;
    }

    public IUIWindow(String windowTitle, ImVec2 relativePosition, ImVec2 size){
        this.windowTitle = windowTitle;
        this.relativePosition = relativePosition;
        this.size = size;
    }

    public void addComponent(IComponent component) {
        components.add(component);
    }

    public void addComponents(List<IComponent> components) {
        this.components = components;
    }

    public void addFlag(int flag) {
        flags |= flag;
    }

    public void removeFlag(int flag) {
        flags &= ~flag;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public void setup(){
        WindowVariables windowVariables = WindowVariables.getInstance();

        if (size != null)
            ImGui.setNextWindowSize(size.x, size.y);
        else
            ImGui.setNextWindowSize(windowVariables.getWidth(), windowVariables.getHeight() - WindowVariables.getInstance().getMainMenuHeight());
        ImGui.setNextWindowPos(windowVariables.getWindowPosX() + relativePosition.x, windowVariables.getWindowPosY() + relativePosition.y+ WindowVariables.getInstance().getMainMenuHeight());

        isFirstTime = false;
    }

    public void render(){
        if(isFirstTime && !isDocked)
            setup();
    }

    public boolean isWindowBeingDragged(){
        return ImGui.isWindowHovered() && ImGui.isMouseDragging(0, 0);
    }

    public ImVec2 calculateWindowPosition(){
        // get the position of the main window
        ImVec2 mainWindowPos = new ImVec2();
        ImGui.getMainViewport().getPos(mainWindowPos);
        // get the position of the top left corner of the window
        ImVec2 position = new ImVec2();
        ImGui.getWindowPos(position);

        position.x = position.x - mainWindowPos.x;
        position.y = position.y - mainWindowPos.y;
        return position;
    }
}
