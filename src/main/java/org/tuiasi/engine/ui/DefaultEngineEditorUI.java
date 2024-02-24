package org.tuiasi.engine.ui;

import imgui.*;
import imgui.flag.ImGuiDir;
import lombok.Getter;
import lombok.Setter;
import org.tuiasi.engine.ui.components.basicComponents.TopMenuBar;
import org.tuiasi.engine.ui.components.basicComponents.textbox.Textbox;
import org.tuiasi.engine.ui.uiWindows.IUIWindow;
import org.tuiasi.engine.ui.uiWindows.UIWindow;
import org.tuiasi.engine.ui.uiWindows.prefabs.UIFilesWindow;
import org.tuiasi.engine.ui.uiWindows.prefabs.UILogsWindow;
import org.tuiasi.engine.ui.uiWindows.prefabs.UINodeInspectorWindow;
import org.tuiasi.engine.ui.uiWindows.prefabs.UINodeTreeWindow;

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

            UILogsWindow debugLogsWindow = new UILogsWindow("Debug logs", new ImVec2(0, 0), new ImVec2(100, 100));
            debugLogsWindow.setDocked(true);
            mainWindow.addDockedWindow(debugLogsWindow, ImGuiDir.Down, 0.2f);
            uiWindows.add(debugLogsWindow);

            UINodeInspectorWindow nodeInspectorWindow = new UINodeInspectorWindow("Node Inspector", new ImVec2(0, 0), new ImVec2(100, 100));
            nodeInspectorWindow.setDocked(true);
            mainWindow.addDockedWindow(nodeInspectorWindow, ImGuiDir.Right, 0.2f);
            uiWindows.add(nodeInspectorWindow);

            UINodeTreeWindow nodeTreeWindow = new UINodeTreeWindow("Node Tree", new ImVec2(0, 0), new ImVec2(100, 100));
            nodeTreeWindow.setDocked(true);
            mainWindow.addDockedWindow(nodeTreeWindow, ImGuiDir.Left, 0.2f);
            uiWindows.add(nodeTreeWindow);

            UIFilesWindow filesWindow = new UIFilesWindow("Files", new ImVec2(0, 0), new ImVec2(100, 100));
            filesWindow.setDocked(true);
            nodeTreeWindow.addDockedWindow(filesWindow, ImGuiDir.Down, 0.2f);
            uiWindows.add(filesWindow);
        }

        public void renderUI() {
            topMenuBar.render();
            for (IUIWindow uiWindow : uiWindows) {
                uiWindow.render();
            }
        }

}
