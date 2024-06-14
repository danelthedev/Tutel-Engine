package org.tuiasi.engine.ui.components.basicComponents;

import imgui.flag.ImGuiColorEditFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.internal.ImGui;
import lombok.Getter;
import lombok.Setter;
import org.tuiasi.engine.global.WindowVariables;
import org.tuiasi.engine.global.nodes.Node;
import org.tuiasi.engine.logic.AppLogic;
import org.tuiasi.engine.logic.EngineState;
import org.tuiasi.engine.logic.logger.Log;
import org.tuiasi.engine.renderer.camera.MainCamera;
import org.tuiasi.engine.renderer.texture.Texture;
import org.tuiasi.engine.ui.DefaultEngineEditorUI;
import org.tuiasi.engine.ui.components.IComponent;
import org.tuiasi.engine.ui.components.composedComponents.Dialog.DialogType;
import org.tuiasi.engine.ui.components.composedComponents.Dialog.FileDialog;
import org.tuiasi.engine.ui.components.composedComponents.Dialog.FileDialogFromButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TopMenuBar extends IComponent {


    FileDialog fileDialog;

    Texture playTexture, stopTexture;

    public TopMenuBar(){
        playTexture = null;
        stopTexture = null;

    }

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
                if(fileDialog == null || !DefaultEngineEditorUI.getPopups().contains(fileDialog)) {
                    fileDialog = new FileDialog("NewProject", DialogType.FOLDER, null, this);
                    DefaultEngineEditorUI.addPopup(fileDialog);
                }
            }
            if(ImGui.menuItem("Open project")) {
                if(fileDialog == null || !DefaultEngineEditorUI.getPopups().contains(fileDialog)) {
                    fileDialog = new FileDialog("OpenProject", DialogType.FILE, null, this);
                    DefaultEngineEditorUI.addPopup(fileDialog);
                }
            }
            if(ImGui.menuItem("Save project")){
                if(AppLogic.getProjectFile() != null)
                    AppLogic.saveProject();
                else
                    Log.error("Select a folder for the project before saving");
            }

            ImGui.endMenu();
        }

        if (ImGui.beginMenu("Window")) {

            if(ImGui.menuItem(DefaultEngineEditorUI.isDisplayingUI() ? "Disable UI" : "Enable UI")){
                DefaultEngineEditorUI.setDisplayingUI(!DefaultEngineEditorUI.isDisplayingUI());
            }

            ImGui.endMenu();
        }

        if(ImGui.button("Export")){
            if(fileDialog == null || !DefaultEngineEditorUI.getPopups().contains(fileDialog)) {
                fileDialog = new FileDialog("Export", DialogType.FOLDER, null, this);
                DefaultEngineEditorUI.addPopup(fileDialog);
            }
        }


        // play button
        ImGui.setCursorPosX((imgui.ImGui.getWindowSizeX() - 30));
        if(playTexture == null) {
            playTexture = new Texture("C:\\Users\\Danel\\IdeaProjects\\licenta\\src\\main\\resources\\textures\\uiTextures\\play.png", 0, true);
            stopTexture = new Texture("C:\\Users\\Danel\\IdeaProjects\\licenta\\src\\main\\resources\\textures\\uiTextures\\stop.png", 0, true);
        }
        Texture currentStateTexture = AppLogic.getEngineState() == EngineState.PLAY ? stopTexture : playTexture;
        if(ImGui.imageButton(currentStateTexture.getTextureID(), 25, 20)){
            if(AppLogic.getEngineState() == EngineState.PLAY){
                AppLogic.setEngineState(EngineState.EDITOR);
                MainCamera.setInstance(AppLogic.getEditorCamera());
                AppLogic.cleanNodeQueue();
                DefaultEngineEditorUI.setDisplayingUI(true);
            }
            else{
                AppLogic.setEngineState(EngineState.PLAY);
                AppLogic.initializeNodes();
                DefaultEngineEditorUI.setDisplayingUI(false);
            }
        }

        ImGui.endMainMenuBar();
        ImGui.popStyleVar();
    }


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
