package org.tuiasi.engine.ui.uiWindows;

import imgui.ImVec2;
import imgui.flag.ImGuiDir;
import imgui.flag.ImGuiStyleVar;
import imgui.internal.flag.ImGuiDockNodeFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.internal.ImGui;
import imgui.type.ImInt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tuiasi.engine.global.WindowVariables;

import java.util.HashMap;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UIDockerWindow extends IUIWindow{

    private boolean firstTime = true;
    private Map<String, Integer> dockedWindows = new HashMap<>();;

    public UIDockerWindow(String windowTitle, ImVec2 relativePosition, ImVec2 size){
        super(windowTitle, relativePosition, size);
    }

    public void addDockedWindow(String windowTitle, int dockSpaceId){
        dockedWindows.put(windowTitle, dockSpaceId);
    }

    @Override
    public void render() {

        // TODO: Make the code look nice and clean!
        // TODO: Add a way to choose a relative window to use for docking
        // TODO: Make the components inside windows fill the window

        super.render();

        WindowVariables windowVariables = WindowVariables.getInstance();

        // top menu bar with 2 options: file and edit

        ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 0, 10);
        ImGui.beginMainMenuBar();

        float mainMenuHeight = ImGui.getWindowSize().y;

        if (ImGui.beginMenu("File")) {
            ImGui.endMenu();
        }
        if (ImGui.beginMenu("Edit")) {
            ImGui.endMenu();
        }
        ImGui.endMainMenuBar();
        ImGui.popStyleVar();

        // main window
        ImGui.setNextWindowPos(windowVariables.getWindowPosX(), windowVariables.getWindowPosY()+mainMenuHeight);

        ImGui.begin(getWindowTitle(), getFlags() | ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoBackground  | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove);
        ImGui.setWindowSize(windowVariables.getWidth(), windowVariables.getHeight() - mainMenuHeight);
        Integer dockspace_id = ImGui.getID("DockSpace");
        ImGui.dockSpace(dockspace_id, 0, 0, ImGuiDockNodeFlags.PassthruCentralNode | ImGuiDockNodeFlags.NoDockingInCentralNode);


        if (firstTime) {
            firstTime = false;

            ImGui.dockBuilderRemoveNode(dockspace_id);
            ImGui.dockBuilderAddNode(dockspace_id, ImGuiDockNodeFlags.PassthruCentralNode | ImGuiDockNodeFlags.NoDockingInCentralNode | ImGuiDockNodeFlags.DockSpace);
            ImGui.dockBuilderSetNodeSize(dockspace_id, windowVariables.getWidth(), windowVariables.getHeight() - mainMenuHeight);

            ImInt newNode = new ImInt(dockspace_id);
            for(Map.Entry<String, Integer> entry : dockedWindows.entrySet()){

                String windowLabel = entry.getKey();
                Integer direction = entry.getValue();

                int dock_id = ImGui.dockBuilderSplitNode(newNode.get(), direction, 0.5f, null, newNode);

                ImGui.dockBuilderDockWindow(windowLabel, dock_id);
            }

            ImGui.dockBuilderFinish(dockspace_id);
        }

        ImGui.end();

    }

}
