package org.tuiasi.engine.ui;

import imgui.ImVec2;
import imgui.flag.ImGuiDir;
import imgui.internal.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.internal.ImGui;
import imgui.internal.ImGuiDockNode;
import imgui.internal.flag.ImGuiDockNodeFlags;
import imgui.internal.flag.ImGuiDockNodeState;
import imgui.type.ImInt;
import lombok.Getter;
import lombok.Setter;
import org.tuiasi.engine.global.WindowVariables;
import org.tuiasi.engine.ui.components.basicComponents.button.ButtonWithTitle;
import org.tuiasi.engine.ui.components.basicComponents.checkbox.CheckboxWithTitle;
import org.tuiasi.engine.ui.components.basicComponents.list.ListWithTitle;
import org.tuiasi.engine.ui.components.basicComponents.textbox.Textbox;
import org.tuiasi.engine.ui.components.basicComponents.tree.TreeNode;
import org.tuiasi.engine.ui.components.composedComponents.TreeWithTitleAndSearchBar;
import org.tuiasi.engine.ui.uiWindows.IUIWindow;
import org.tuiasi.engine.ui.uiWindows.UIDockerWindow;
import org.tuiasi.engine.ui.uiWindows.UIWindow;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class DefaultEngineEditorUI {

        List<IUIWindow> uiWindows;
        boolean isSetup = false;

        public DefaultEngineEditorUI() {
            uiWindows = new ArrayList<>();

            UIDockerWindow mainWindow = new UIDockerWindow("Main Window", new ImVec2(0, 0), null);
            uiWindows.add(mainWindow);

            UIWindow sceneNodes = new UIWindow("Scene Nodes", new ImVec2(0, 0), new ImVec2(100, 100));
            sceneNodes.setDocked(true);
            sceneNodes.addComponent(
                    new TreeWithTitleAndSearchBar("Search Nodes", "Scene Nodes",
                            List.of(
                                    new TreeNode("Node1", List.of(new TreeNode("Node1.1", List.of(new TreeNode("Node1.1.1", List.of()))
                                    ))))
            , null, null));
            uiWindows.add(sceneNodes);

            UIWindow files = new UIWindow("Files", new ImVec2(0, 0), new ImVec2(100, 100));
            files.setDocked(true);
            files.addComponent(new ListWithTitle("", List.of("File1", "File2", "File3")));
            uiWindows.add(files);

            UIWindow logs = new UIWindow("Logs", new ImVec2(0, 0), new ImVec2(100, 100));
            logs.setDocked(true);
            logs.addComponent(new Textbox("Logs"));
            uiWindows.add(logs);

            UIWindow nodeDetails = new UIWindow("Node Details", new ImVec2(0, 0), new ImVec2(100, 100));
            nodeDetails.setDocked(true);
            // add some buttons and checkboxes
            nodeDetails.addComponent(new ButtonWithTitle("Button1"));
            nodeDetails.addComponent(new CheckboxWithTitle("Cool checkbox", false));

            uiWindows.add(nodeDetails);

            mainWindow.addDockedWindow(sceneNodes.getWindowTitle(), ImGuiDir.Left);
            mainWindow.addDockedWindow(files.getWindowTitle(), ImGuiDir.Left);
            mainWindow.addDockedWindow(nodeDetails.getWindowTitle(), ImGuiDir.Right);
            mainWindow.addDockedWindow(logs.getWindowTitle(), ImGuiDir.Down);
        }

        public void renderUI() {
            for (IUIWindow uiWindow : uiWindows) {
                uiWindow.render();

            }
        }

}
