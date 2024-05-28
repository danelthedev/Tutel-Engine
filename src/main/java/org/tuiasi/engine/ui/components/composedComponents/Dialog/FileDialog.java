package org.tuiasi.engine.ui.components.composedComponents.Dialog;

import imgui.ImGui;
import imgui.extension.imguifiledialog.ImGuiFileDialog;
import imgui.extension.imguifiledialog.flag.ImGuiFileDialogFlags;
import imgui.type.ImString;
import lombok.Getter;
import lombok.Setter;
import org.tuiasi.engine.global.nodes.Node;
import org.tuiasi.engine.ui.DefaultEngineEditorUI;
import org.tuiasi.engine.ui.components.IComponent;
import org.tuiasi.engine.ui.components.basicComponents.TopMenuBar;
import org.tuiasi.engine.ui.components.basicComponents.searchbar.SearchbarWithHint;
import org.tuiasi.engine.ui.uiWindows.prefabs.UIFilesWindow;

import java.io.File;
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
    public void render() {
        if(dialogType == DialogType.FILE) {
            if (!isActive) {
                isActive = true;
                ImGuiFileDialog.openModal("browse-key", "Choose File", ".*", ".", 1, 42, ImGuiFileDialogFlags.None);
            }
            if (ImGuiFileDialog.display("browse-key", ImGuiFileDialogFlags.None, 200, 400, 800, 600)) {
                if (ImGuiFileDialog.isOk()) {
                    selection = ImGuiFileDialog.getSelection();
                    userData = ImGuiFileDialog.getUserDatas();
                }
                ImGuiFileDialog.close();
                DefaultEngineEditorUI.removePopup(this);
            }
        }

        if(dialogType == DialogType.FOLDER) {
            if (!isActive) {
               isActive = true;
               ImGuiFileDialog.openDialog("browse-folder-key", "Choose Folder", null, ".", "", 1, 7, ImGuiFileDialogFlags.None);
           }
           if (ImGuiFileDialog.display("browse-folder-key", ImGuiFileDialogFlags.None, 200, 400, 800, 600)) {
               if (ImGuiFileDialog.isOk()) {
                   selection = ImGuiFileDialog.getSelection();
                   userData = ImGuiFileDialog.getUserDatas();

                   // TODO: Clean up this code and file dialog in general
                   if(originator != null && originator instanceof TopMenuBar) {
                       Optional<String> path = selection.values().stream().findFirst();
                       String currentDirectory = System.getProperty("user.dir");
                       int i = 0;
                       if(path.isPresent())
                           i = path.get().lastIndexOf("\\");
                       String workPath = path.isPresent() && !path.get().endsWith(".") ? path.get().substring(0, i) : currentDirectory;
                       String folderName = path.isPresent() ? path.get().substring(i + 1) : currentDirectory.substring(currentDirectory.lastIndexOf("\\") + 1);

                       ((TopMenuBar) originator).setWorkDirectory(workPath);

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