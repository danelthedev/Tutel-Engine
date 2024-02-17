package org.tuiasi.engine.ui.basicComponents.searchbar;

import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiKey;
import imgui.type.ImString;
import lombok.*;
import org.tuiasi.engine.ui.basicComponents.tree.ITree;

@Getter
@Setter
@NoArgsConstructor
public class SearchbarWithHint implements ISearchbar {

    private String hint = "Search";
    private ImString searchText = new ImString();
    private boolean enterPressed = false;

    private SearchListener searchListener;

    public SearchbarWithHint(String hint) {
        this.hint = hint;
    }

    @Override
    public void render() {
        enterPressed = ImGui.inputTextWithHint("##searchbar", hint, searchText, ImGuiInputTextFlags.EnterReturnsTrue);
        ImGui.sameLine();
        if (enterPressed && searchListener != null) {
            searchListener.onSearch(searchText.get());
        }
    }

}