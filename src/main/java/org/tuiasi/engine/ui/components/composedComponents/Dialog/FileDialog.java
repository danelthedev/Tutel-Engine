package org.tuiasi.engine.ui.components.composedComponents.Dialog;

import imgui.ImGui;
import imgui.extension.imguifiledialog.ImGuiFileDialog;
import imgui.extension.imguifiledialog.flag.ImGuiFileDialogFlags;
import imgui.type.ImString;
import lombok.Getter;
import lombok.Setter;
import org.tuiasi.engine.global.nodes.Node;
import org.tuiasi.engine.logic.AppLogic;
import org.tuiasi.engine.logic.logger.Log;
import org.tuiasi.engine.ui.DefaultEngineEditorUI;
import org.tuiasi.engine.ui.components.IComponent;
import org.tuiasi.engine.ui.components.basicComponents.TopMenuBar;
import org.tuiasi.engine.ui.components.basicComponents.searchbar.SearchbarWithHint;
import org.tuiasi.engine.ui.uiWindows.prefabs.UIFilesWindow;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Getter @Setter
public class FileDialog extends IComponent {

    private String label;
    private static Map<String, String> selection = null;
    private static long userData = 0;
    DialogType dialogType = DialogType.FILE;

    private boolean isActive;
    private SearchbarWithHint relatedSearchbar;

    private IComponent originator;

    public FileDialog(String label, DialogType dialogType, SearchbarWithHint relatedSearchbar, IComponent originator) {
        this.label = label;
        this.dialogType = dialogType;
        this.relatedSearchbar = relatedSearchbar;
        this.originator = originator;
    }

    @Override
    public void render(){
        if(dialogType == DialogType.FILE) {
            if (!isActive) {
                isActive = true;
                ImGuiFileDialog.openModal("browse-key##" + label, "Choose File", ".*", AppLogic.getWorkingDirectory(), 1, 42, ImGuiFileDialogFlags.None);
            }
            if (ImGuiFileDialog.display("browse-key##" + label, ImGuiFileDialogFlags.None, 200, 400, 800, 600)) {
                if (ImGuiFileDialog.isOk()) {
                    selection = ImGuiFileDialog.getSelection();
                    userData = ImGuiFileDialog.getUserDatas();
                    Log.info(selection.values().stream().findFirst().get());

                    if(selection.values().stream().findFirst().isPresent() && selection.values().stream().findFirst().get().endsWith(".tutel")) {
                        // AppLogic.loadProject(selection.values().stream().findFirst().get());

                        AppLogic.setWorkingDirectory(selection.values().stream().findFirst().get().substring(0, selection.values().stream().findFirst().get().lastIndexOf("\\")));
                        AppLogic.setProjectFile(new File(selection.values().stream().findFirst().get()));
                        AppLogic.loadProject();

                        String workPath = AppLogic.getWorkingDirectory();
                        // get last folder in the work path
                        int i = workPath.lastIndexOf("\\");
                        String folderName = workPath.substring(i + 1);

                        Node<File> rootNode = new Node<>(null, folderName, new File(workPath));
                        ((TopMenuBar) originator).listf(workPath, rootNode);
                        UIFilesWindow treeWindow = (UIFilesWindow) DefaultEngineEditorUI.getWindow("Files"); // Get the UIFilesWindow instance
                        if(treeWindow != null)
                            treeWindow.getTreeComponent().getTree().setRoot(rootNode);
                    }

                }
                ImGuiFileDialog.close();
                DefaultEngineEditorUI.removePopup(this);
            }
        }

        if(dialogType == DialogType.FOLDER) {
            if (!isActive) {
                isActive = true;
                ImGuiFileDialog.openDialog("browse-folder-key##" + label, "Choose Folder", null, AppLogic.getWorkingDirectory(), "", 1, 7, ImGuiFileDialogFlags.None);
            }
            if (ImGuiFileDialog.display("browse-folder-key##" + label, ImGuiFileDialogFlags.None, 200, 400, 800, 600)) {
                if (ImGuiFileDialog.isOk()) {
                    selection = ImGuiFileDialog.getSelection();
                    userData = ImGuiFileDialog.getUserDatas();

                    if(label.equals("NewProject")) {
                        Optional<String> path = selection.values().stream().findFirst();
                        String currentDirectory = AppLogic.getWorkingDirectory();
                        int i = 0;
                        if(path.isPresent())
                            i = path.get().lastIndexOf("\\");
                        String workPath = path.isPresent() && !path.get().endsWith(".") ? path.get().substring(0, i) : currentDirectory;
                        String folderName = path.isPresent() ? path.get().substring(i + 1) : currentDirectory.substring(currentDirectory.lastIndexOf("\\") + 1);

                        AppLogic.setWorkingDirectory(workPath);
                        Log.info("Working directory set to: " + workPath);

                        // create a file called "project.tutel" in the selected folder
                        File projectFile = new File(workPath + "\\project.tutel");
                        AppLogic.setProjectFile(projectFile);

                        if(!projectFile.exists()) {
                            try {
                                projectFile.createNewFile();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        // create assets folder
                        File assetsFolder = new File(workPath + "\\assets");

                        if(!assetsFolder.exists()) {
                            assetsFolder.mkdir();
                        }

                        Node<File> rootNode = new Node<>(null, folderName, new File(workPath));
                        ((TopMenuBar) originator).listf(workPath, rootNode);
                        UIFilesWindow treeWindow = (UIFilesWindow) DefaultEngineEditorUI.getWindow("Files"); // Get the UIFilesWindow instance
                        if(treeWindow != null)
                            treeWindow.getTreeComponent().getTree().setRoot(rootNode);
                    }

                    if(label.equals("Export")){
                        Optional<String> path = selection.values().stream().findFirst();
                        String currentDirectory = AppLogic.getWorkingDirectory();
                        int i = 0;
                        if(path.isPresent())
                            i = path.get().lastIndexOf("\\");
                        String workPath = path.isPresent() && !path.get().endsWith(".") ? path.get().substring(0, i) : currentDirectory;
                        String folderName = path.isPresent() ? path.get().substring(i + 1) : currentDirectory.substring(currentDirectory.lastIndexOf("\\") + 1);

                        Log.info("Exporting to game to: " + workPath);

                        try {
                            AppLogic.copyJarFile(workPath + "/game.jar");
                        } catch (IOException e) {
                            Log.error("Error exporting game to: " + workPath + "/game.jar");
                        }


                    }

                }
                ImGuiFileDialog.close();
                DefaultEngineEditorUI.removePopup(this);
            }
        }

        if(isSeparator())
            ImGui.separator();

        if (selection != null && !selection.isEmpty() && relatedSearchbar != null && isActive) {
            relatedSearchbar.setSearchText(new ImString(selection.values().stream().findFirst().get()));
            relatedSearchbar.trigger();

            selection = null;
            userData = 0;
            isActive = false;
        }

    }

}