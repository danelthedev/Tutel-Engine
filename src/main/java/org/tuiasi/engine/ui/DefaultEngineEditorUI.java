package org.tuiasi.engine.ui;

import imgui.ImVec2;
import imgui.flag.ImGuiDir;
import lombok.Getter;
import lombok.Setter;
import org.tuiasi.engine.ui.components.IComponent;
import org.tuiasi.engine.ui.components.basicComponents.TopMenuBar;
import org.tuiasi.engine.ui.components.composedComponents.Dialog.DialogType;
import org.tuiasi.engine.ui.components.composedComponents.Dialog.FileDialog;
import org.tuiasi.engine.ui.uiWindows.IUIWindow;
import org.tuiasi.engine.ui.uiWindows.UIWindow;
import org.tuiasi.engine.ui.uiWindows.prefabs.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class DefaultEngineEditorUI {

        private TopMenuBar topMenuBar;
        private static List<IUIWindow> uiWindows;
        boolean isSetup = false;

        IComponent fileDialog;

        public DefaultEngineEditorUI() {
            uiWindows = new ArrayList<>();

            topMenuBar = new TopMenuBar();
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
            for (int i = 0; i < uiWindows.size(); i++) {
                uiWindows.get(i).render();
            }
        }

        public static void addWindow(IUIWindow window) {
            uiWindows.add(window);
        }

        public static void removeWindow(IUIWindow window) {
            uiWindows.remove(window);
        }

        public static IUIWindow getWindow(String windowTitle) {
            for (IUIWindow uiWindow : uiWindows) {
                if (uiWindow.getWindowTitle().equals(windowTitle)) {
                    return uiWindow;
                }
            }
            return null;
        }

}
