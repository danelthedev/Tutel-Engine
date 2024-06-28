package org.tuiasi.engine.ui.components.basicComponents.textbox;

import org.tuiasi.engine.ui.components.IListener;

public interface TextboxListener extends IListener {
    void onTextChange(String text);
}