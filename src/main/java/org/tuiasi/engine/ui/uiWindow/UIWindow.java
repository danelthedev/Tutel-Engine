package org.tuiasi.engine.ui.uiWindow;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tuiasi.engine.ui.SnapPosition;
import org.tuiasi.engine.ui.components.IComponent;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class UIWindow implements IUIWindow{

    private String windowTitle;
    private boolean dragged = false;
    private ImVec2 windowPosition = new ImVec2();
    private int flags = 0;
    private List<IComponent> components = new ArrayList<>();

    private SnapPosition snapPosition = SnapPosition.NONE;

    public UIWindow(String windowTitle) {
        this.windowTitle = windowTitle;
    }

    public void addComponent(IComponent component) {
        components.add(component);
    }

    public void addComponents(List<IComponent> components) {
        this.components = components;
    }

    @Override
    public void render() {

        ImGui.begin(windowTitle, flags);

        dragged = isWindowBeingDragged();
        if(dragged)
            windowPosition = calculateWindowPosition();

        for (IComponent component : components) {
            component.render();
        }

        ImGui.end();
    }

    @Override
    public void addFlag(int flag) {
        flags |= flag;
    }

    @Override
    public void removeFlag(int flag) {
        flags &= ~flag;
    }

    public boolean isWindowBeingDragged(){
        return ImGui.isWindowHovered() && ImGui.isMouseDragging(0, 0);
    }

    public ImVec2 calculateWindowPosition(){
        ImVec2 mainWindowPos = new ImVec2();
        ImGui.getMainViewport().getPos(mainWindowPos);

        ImVec2 position = new ImVec2();
        ImGui.getWindowPos(position);

        position.x = position.x - mainWindowPos.x;
        position.y = position.y - mainWindowPos.y;
        return position;
    }

}
