package org.tuiasi.engine.ui.components.basicComponents.textbox;

import imgui.ImGui;
import imgui.ImGuiInputTextCallbackData;
import imgui.callback.ImGuiInputTextCallback;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImString;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tuiasi.engine.global.IO.KeyboardHandler;

@Getter @Setter @NoArgsConstructor
public class Textbox extends ITextbox{

    private String label;
    private ImString text = new ImString();
    boolean enterPressed = false;
    TextboxListener textboxListener;
    private int fontSize = 50;

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

        // Callback needed for buffer size override
        enterPressed = ImGui.inputTextMultiline("##Textbox_" + label, text, getWidth() * ImGui.getWindowWidth(), getHeight() * ImGui.getWindowHeight(), getFlags() | ImGuiInputTextFlags.CallbackResize, new ImGuiInputTextCallback() {
            @Override
            public void accept(ImGuiInputTextCallbackData imGuiInputTextCallbackData) {
                // TODO: Implement some sort of text wrap
            }
        });

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
