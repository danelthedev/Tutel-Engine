package org.tuiasi.engine.ui.basicComponents.textbox;

public interface ITextbox {

    void render();
    void setText(String text);

    interface TextboxListener {
        void onTextChange(String text);
    }
}
