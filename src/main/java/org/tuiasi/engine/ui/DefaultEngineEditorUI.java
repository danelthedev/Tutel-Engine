package org.tuiasi.engine.ui;

import imgui.ImGui;
import lombok.Getter;
import lombok.Setter;
import org.tuiasi.engine.ui.uiWindow.DemoWindow;

@Getter @Setter
public class DefaultEngineEditorUI {

        private float[] windowPosition = { 0.0f, 0.0f }; // Adjust these values as needed

        DemoWindow demoWindow;

        public DefaultEngineEditorUI() {
            demoWindow = new DemoWindow("Demo window");
        }

        public void renderUI() {

            ImGui.begin("Engine Editor");

            demoWindow.render();

            ImGui.end();

        }

        // Method to set the window position
        public void setUIPosition(float x, float y) {
            windowPosition[0] = x;
            windowPosition[1] = y;
        }

}
