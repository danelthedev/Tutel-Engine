package org.tuiasi.engine.ui.components.basicComponents.textbox;

import imgui.ImGui;
import imgui.ImGuiInputTextCallbackData;
import imgui.callback.ImGuiInputTextCallback;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tuiasi.engine.logic.IO.KeyboardHandler;

@Getter @Setter @NoArgsConstructor
public class Textbox extends ITextbox{

    private String label;
    private ImString text = new ImString(2000);
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

        // calculate the height of the textbox based on the font size and the number of lines in the text
        float textHeight = ImGui.calcTextSize(text.get()).y;
        float windowHeight = ImGui.getWindowHeight();
        float height = Math.max(windowHeight, textHeight);

        enterPressed = ImGui.inputTextMultiline("##Textbox_" + label, text, ImGui.getWindowWidth(), height, getFlags() | ImGuiInputTextFlags.CallbackResize, new ImGuiInputTextCallback() {
            @Override
            public void accept(ImGuiInputTextCallbackData imGuiInputTextCallbackData) {
            }
        });

        if(previousText.length() > 1000) {
            text = new ImString(2000);
            previousText = "";
        }

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
