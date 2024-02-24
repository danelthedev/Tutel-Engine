package org.tuiasi.engine.ui;

import imgui.ImVec2;
import imgui.flag.ImGuiDir;
import lombok.Getter;
import lombok.Setter;
import org.tuiasi.engine.ui.components.basicComponents.TopMenuBar;
import org.tuiasi.engine.ui.uiWindows.IUIWindow;
import org.tuiasi.engine.ui.uiWindows.UIWindow;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class DefaultEngineEditorUI {

        private TopMenuBar topMenuBar;
        private List<IUIWindow> uiWindows;
        boolean isSetup = false;

        public DefaultEngineEditorUI() {
            topMenuBar = new TopMenuBar();

            uiWindows = new ArrayList<>();

            UIWindow mainWindow = new UIWindow("Main Window", new ImVec2(0, 0), null, true);
            mainWindow.setDocked(true);
            uiWindows.add(mainWindow);

            UIWindow debugLogsWindow = new UIWindow("Debug logs", new ImVec2(0, 0), new ImVec2(100, 100));
            debugLogsWindow.setDocked(true);
            mainWindow.addDockedWindow("Debug logs", ImGuiDir.Down, 0.2f);
            uiWindows.add(debugLogsWindow);

            UIWindow nodeTreeWindow = new UIWindow("Node Tree", new ImVec2(0, 0), new ImVec2(100, 100));
            nodeTreeWindow.setDocked(true);
            mainWindow.addDockedWindow("Node Tree", ImGuiDir.Left, 0.2f);
            uiWindows.add(nodeTreeWindow);

            UIWindow filesWindow = new UIWindow("Files", new ImVec2(0, 0), new ImVec2(100, 100));
            filesWindow.setDocked(true);
            nodeTreeWindow.addDockedWindow("Files", ImGuiDir.Down, 0.3f);
            uiWindows.add(filesWindow);

            UIWindow nodeDetailsWindow = new UIWindow("Node Details", new ImVec2(0, 0), new ImVec2(100, 100));
            nodeDetailsWindow.setDocked(true);
            mainWindow.addDockedWindow("Node Details", ImGuiDir.Right, 0.2f);
            uiWindows.add(nodeDetailsWindow);


        }

        public void renderUI() {
            topMenuBar.render();
            for (IUIWindow uiWindow : uiWindows) {
                uiWindow.render();
            }
        }

}
