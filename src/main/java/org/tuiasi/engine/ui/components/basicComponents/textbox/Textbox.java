package org.tuiasi.engine.ui.components.basicComponents.textbox;

import imgui.ImGui;
import imgui.type.ImString;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tuiasi.engine.global.IO.KeyboardHandler;

@Getter @Setter @NoArgsConstructor
public class Textbox implements ITextbox{

    private String label;
    private ImString text = new ImString();
    boolean enterPressed = false;
    TextboxListener textboxListener;

    String previousText = "";

    public Textbox(String label) {
        this.label = label;
    }

    public Textbox(String label, TextboxListener textboxListener) {
        this.label = label;
        this.textboxListener = textboxListener;
    }

    @Override
    public void render() {
        previousText = text.get();

        enterPressed = ImGui.inputTextMultiline("##Textbox_" + label, text);
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
