package org.tuiasi.engine.ui.components.composedComponents.Dialog;

import imgui.ImGui;
import imgui.extension.imguifiledialog.ImGuiFileDialog;
import imgui.extension.imguifiledialog.flag.ImGuiFileDialogFlags;
import imgui.flag.ImGuiCond;
import imgui.type.ImString;
import lombok.Getter;
import lombok.Setter;
import org.tuiasi.engine.global.WindowVariables;
import org.tuiasi.engine.logic.AppLogic;
import org.tuiasi.engine.ui.components.IComponent;
import org.tuiasi.engine.ui.components.basicComponents.searchbar.SearchbarWithHint;

import java.util.Map;

@Getter @Setter
public class FileDialogFromButton extends IComponent {

    private String label;
    private static Map<String, String> selection = null;
    private static long userData = 0;
    DialogType dialogType = DialogType.FILE;
    private String filter = ".*";

    private Boolean isActive = true;
    private SearchbarWithHint relatedSearchbar;

    public FileDialogFromButton(String label, DialogType dialogType, SearchbarWithHint relatedSearchbar) {
        this.label = label;
        this.dialogType = dialogType;
        this.relatedSearchbar = relatedSearchbar;
    }

    public FileDialogFromButton(String label, DialogType dialogType, SearchbarWithHint relatedSearchbar, String filter) {
        this.label = label;
        this.dialogType = dialogType;
        this.relatedSearchbar = relatedSearchbar;
        this.filter = filter;
    }

    @Override
    public void render() {
        if(dialogType == DialogType.FILE) {
            ImGui.sameLine();
            if (ImGui.button("Browse##" + label)) {
                isActive = true;

                // using ImGui.setNextWindowPos set the position of the modal to the center of the screen
                ImGui.setNextWindowPos((float) WindowVariables.getInstance().getWidth() / 2, (float) WindowVariables.getInstance().getHeight() / 2, ImGuiCond.Appearing);

                ImGuiFileDialog.openModal("browse-key##" + label, "Choose File", filter, AppLogic.getWorkingDirectory(), 1, 42, ImGuiFileDialogFlags.None);
            }
            if (ImGuiFileDialog.display("browse-key##" + label, ImGuiFileDialogFlags.None, 200, 400, 800, 600)) {
                if (ImGuiFileDialog.isOk()) {
                    selection = ImGuiFileDialog.getSelection();
                    userData = ImGuiFileDialog.getUserDatas();

                }
                ImGuiFileDialog.close();
            }
        }

        if(dialogType == DialogType.FOLDER) {
           if (ImGui.button("Browse##" + label)) {
               isActive = true;
               ImGuiFileDialog.openDialog("browse-folder-key1", "Choose Folder", null, ".", "", 1, 7, ImGuiFileDialogFlags.None);
           }
           if (ImGuiFileDialog.display("browse-folder-key1", ImGuiFileDialogFlags.None, 200, 400, 800, 600)) {
               if (ImGuiFileDialog.isOk()) {
                   selection = ImGuiFileDialog.getSelection();
                   userData = ImGuiFileDialog.getUserDatas();
               }
               ImGuiFileDialog.close();
           }
        }

        if(isSeparator())
            ImGui.separator();

        if (selection != null && !selection.isEmpty() && relatedSearchbar != null && isActive && (selection.values().stream().findFirst().get().endsWith(ImGuiFileDialog.getCurrentFilter()) || filter.equals(".*"))) {
            relatedSearchbar.setSearchText(new ImString(selection.values().stream().findFirst().get()));
            relatedSearchbar.trigger();

            selection = null;
            userData = 0;
            isActive = false;
        }

    }

}