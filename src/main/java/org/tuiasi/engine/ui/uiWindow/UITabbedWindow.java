package org.tuiasi.engine.ui.uiWindow;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tuiasi.engine.global.WindowVariables;
import org.tuiasi.engine.ui.SnapPosition;
import org.tuiasi.engine.ui.components.IComponent;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class UITabbedWindow implements IUIWindow {

    private String windowTitle;
    private boolean dragged = false;
    private ImVec2 windowPosition = new ImVec2();
    private int flags = 0;
    private List<IUIWindow> tabWindows = new ArrayList<>();
    private int selectedTabIndex = 0;
    private List<IComponent> components = new ArrayList<>();

    private SnapPosition snapPosition = SnapPosition.NONE;

    public UITabbedWindow(String windowTitle) {
        this.windowTitle = windowTitle;
    }

    public void addTab(UIWindow window) {
        tabWindows.add(window);
    }

    public void addTabs(List<IUIWindow> windows) {
        tabWindows.addAll(windows);
    }

    @Override
    public void render() {

        ImGui.begin(windowTitle, flags);

        dragged = isWindowBeingDragged();
        if(dragged)
            windowPosition = calculateWindowPosition();

        checkSnapping();



        if(ImGui.beginTabBar(windowTitle))
        {
            // iterate through the window titles
            for(int i = 0; i < tabWindows.size(); i++) {
                if (ImGui.beginTabItem(tabWindows.get(i).getWindowTitle())) {
                    components = tabWindows.get(i).getComponents();
                    // iterate through the components of the window
                    for(int j = 0; j < tabWindows.get(i).getComponents().size(); j++) {
                        components.get(j).render();
                    }

                    ImGui.endTabItem();
                }
            }

            ImGui.endTabBar();
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

    public void checkSnapping(){
        if(isDragged()){
            ImVec2 size = new ImVec2();
            ImGui.getWindowSize(size);

            // check if any of the edges of the window are close to the edges of the main window (within 100 pixels)
            if(windowPosition.x < 100){
                snapPosition = SnapPosition.LEFT;
            } else if(windowPosition.x > ImGui.getMainViewport().getSizeX() - size.x - 100){
                snapPosition = SnapPosition.RIGHT;
            } else if(windowPosition.y < 100){
                snapPosition = SnapPosition.TOP;
            } else if(windowPosition.y > ImGui.getMainViewport().getSizeY() - size.y - 100){
                snapPosition = SnapPosition.BOTTOM;
            } else {
                snapPosition = SnapPosition.NONE;
            }
        }
        else{
            // if the window is not being dragged, set the position of the window depending on the snap position
            ImVec2 size = new ImVec2();
            ImGui.getWindowSize(size);

            WindowVariables windowVariables = WindowVariables.getInstance();

            switch(snapPosition){
                case LEFT:
                    ImGui.setWindowPos(windowVariables.getWindowPosX(), windowVariables.getWindowPosY() + windowVariables.getHeight() / 2 - size.y / 2);
                    break;
                case RIGHT:
                    ImGui.setWindowPos(windowVariables.getWindowPosX() + windowVariables.getWidth() - size.x, windowVariables.getWindowPosY() + windowVariables.getHeight() / 2 - size.y / 2);
                    break;
                case TOP:
                    ImGui.setWindowPos(windowVariables.getWindowPosX() + windowVariables.getWidth() / 2 - size.x / 2, windowVariables.getWindowPosY());
                    break;
                case BOTTOM:
                    ImGui.setWindowPos(windowVariables.getWindowPosX() + windowVariables.getWidth() / 2 - size.x / 2, windowVariables.getWindowPosY() + windowVariables.getHeight() - size.y);
                    break;
                case NONE:
                    break;
            }
            System.out.println(snapPosition);
        }
    }
}