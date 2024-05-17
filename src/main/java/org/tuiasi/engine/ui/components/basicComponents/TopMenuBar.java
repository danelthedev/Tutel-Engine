package org.tuiasi.engine.ui.components.basicComponents;

import imgui.flag.ImGuiStyleVar;
import imgui.internal.ImGui;
import lombok.Getter;
import lombok.Setter;
import org.tuiasi.engine.global.WindowVariables;
import org.tuiasi.engine.global.nodes.Node;
import org.tuiasi.engine.ui.DefaultEngineEditorUI;
import org.tuiasi.engine.ui.components.IComponent;
import org.tuiasi.engine.ui.components.composedComponents.Dialog.DialogType;
import org.tuiasi.engine.ui.components.composedComponents.Dialog.FileDialog;

import java.io.File;

public class TopMenuBar extends IComponent {


    FileDialog fileDialog;
    @Getter @Setter
    String workDirectory = "";

    public String getLabel() {
        return "TopMenuBar";
    }

    @Override
    public void setLabel(String label) {
    }

    @Override
    public void render() {
        // top menu bar with 2 options: file and edit

        ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 0, 10);
        ImGui.beginMainMenuBar();

        WindowVariables.getInstance().setMainMenuHeight(ImGui.getWindowSize().y);

        if (ImGui.beginMenu("File")) {

            if(ImGui.menuItem("New project")){
                System.out.println("Starting new project");
                if(fileDialog == null || !DefaultEngineEditorUI.getPopups().contains(fileDialog)) {
                    fileDialog = new FileDialog("NewProject", DialogType.FOLDER, null, this);
                    DefaultEngineEditorUI.addPopup(fileDialog);
                }
            }
            if(ImGui.menuItem("Open project")) {
                System.out.println("Opening project");
                if(fileDialog == null || !DefaultEngineEditorUI.getPopups().contains(fileDialog)) {
                    fileDialog = new FileDialog("OpenProject", DialogType.FOLDER, null, this);
                    DefaultEngineEditorUI.addPopup(fileDialog);
                }
            }
            ImGui.endMenu();
        }

        if (ImGui.beginMenu("Edit")) {
            ImGui.endMenu();
        }


        ImGui.endMainMenuBar();
        ImGui.popStyleVar();
    }

    // TODO: Use this to create the node file structure
    public void listf(String directoryName, Node<File> parentNode) {
        File directory = new File(directoryName);

        // Get all files from a directory.
        File[] fList = directory.listFiles();
        if(fList != null)
            for (File file : fList) {
                Node<File> fileNode = new Node<>(parentNode, file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\") + 1), file);
                if (file.isDirectory()) {
                    listf(file.getAbsolutePath(), fileNode);
                }
            }
    }

}
