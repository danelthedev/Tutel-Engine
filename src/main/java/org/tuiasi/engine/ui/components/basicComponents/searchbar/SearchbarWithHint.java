package org.tuiasi.engine.ui.components.basicComponents.searchbar;

import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImString;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchbarWithHint extends ISearchbar {

    private String label;
    private String hint = "Search";
    private ImString searchText = new ImString(50);
    private boolean enterPressed = false;

    private boolean isPassword = false;
    private boolean isEditable = true;

    private SearchListener searchListener;

    public SearchbarWithHint(String hint) {
        this.hint = hint;
    }

    public SearchbarWithHint(String label, String hint) {
        this.label = label;
        this.hint = hint;
    }

    public SearchbarWithHint(String label, String hint, boolean isPassword) {
        this.label = label;
        this.hint = hint;
        this.isPassword = isPassword;
    }

    public void trigger() {
        if (searchListener != null) {
            searchListener.onSearch(searchText.get());
        }
    }

    @Override
    public void render() {

        if(getRatioX() != 0 && getRatioY() != 0 && getWidth() != 0 && getHeight() != 0) {
            ImGui.setCursorPosX((ImGui.getWindowSizeX() - getWidth()) * getRatioX());
            ImGui.setCursorPosY((ImGui.getWindowSizeY() - getHeight()) * getRatioY());
        }

        int flags = isPassword ? ImGuiInputTextFlags.Password : 0;
        flags |= isEditable ? ImGuiInputTextFlags.EnterReturnsTrue : ImGuiInputTextFlags.ReadOnly;

        enterPressed = ImGui.inputTextWithHint("##Searchbar_" + label, hint, searchText, flags);
        ImGui.sameLine();
        if (enterPressed && searchListener != null) {
            searchListener.onSearch(searchText.get());
        }
        ImGui.newLine();
        if(getSearator())
            ImGui.separator();
    }

}