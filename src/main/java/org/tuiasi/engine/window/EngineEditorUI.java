package org.tuiasi.engine.window;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EngineEditorUI {

    private boolean showText = false;

    public void renderUI(){
        //create a imgui window that has fixed size and sticks to the top left corner
        //set position to 0,0 and size to 300,300
        ImGui.begin("Hello, world!", ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoCollapse);
        ImGui.setWindowPos(0,0);
        ImGui.setWindowSize(300,300);
        ImGui.text("This is some useful text.");               // Display some text (you can use a format strings too)
        ImGui.checkbox("Demo Window", showText);      // Edit bools storing our window open/close state

        ImGui.end();

    }
}
