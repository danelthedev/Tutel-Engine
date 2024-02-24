package org.tuiasi.engine.ui.components.basicComponents.searchbar;

import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImString;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class SearchbarWithHint extends ISearchbar {

    private String label;
    private String hint = "Search";
    private ImString searchText = new ImString();
    private boolean enterPressed = false;

    private SearchListener searchListener;

    public SearchbarWithHint(String hint) {
        this.hint = hint;
    }

    public SearchbarWithHint(String label, String hint) {
        this.label = label;
        this.hint = hint;
    }

    @Override
    public void render() {
        enterPressed = ImGui.inputTextWithHint("##Searchbar_" + label, hint, searchText, ImGuiInputTextFlags.EnterReturnsTrue);
        ImGui.sameLine();
        if (enterPressed && searchListener != null) {
            searchListener.onSearch(searchText.get());
        }
    }

}