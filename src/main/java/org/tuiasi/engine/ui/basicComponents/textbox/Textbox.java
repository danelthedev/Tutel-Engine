package org.tuiasi.engine.ui.basicComponents.textbox;

import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImString;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tuiasi.engine.global.IO.KeyboardHandler;
import org.tuiasi.engine.ui.basicComponents.searchbar.ISearchbar;

@Getter @Setter @NoArgsConstructor
public class Textbox implements ITextbox{

    private ImString text = new ImString();
    boolean enterPressed = false;
    TextboxListener textboxListener;

    String previousText = "";

    public Textbox(String text, TextboxListener textboxListener) {
        this.text.set(text);
        this.textboxListener = textboxListener;
    }

    @Override
    public void render() {
        previousText = text.get();

        enterPressed = ImGui.inputTextMultiline("##textbox", text);
        ImGui.sameLine();

        if(ImGui.isItemActive() && KeyboardHandler.isAnyKeyPressed() && textboxListener != null){
            if(!previousText.equals(text.get()))
                textboxListener.onTextChange(text.get());
        }

    }

    public void setText(String text){
        this.text.set(text);
    }

}
