package org.tuiasi.engine.window;

import imgui.ImGui;

public class EngineEditorUI {

    private boolean showText = false;

    public void renderUI(){
        ImGui.begin("Hello, world!"); // Start the Dear ImGui frame

        if(ImGui.button("Im a banana")) {
            showText = true;
            ImGui.sameLine();
            if(ImGui.button("Im a potato")) {
                showText = false;
            }
        }

        if(showText) {
            ImGui.text("I'm a text label!");
        }

        ImGui.end();
    }
}
