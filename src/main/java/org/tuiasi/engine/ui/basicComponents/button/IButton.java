package org.tuiasi.engine.ui.basicComponents.button;

public interface IButton {
    void render();

    interface ButtonListener {
        void onClick();
    }
}
